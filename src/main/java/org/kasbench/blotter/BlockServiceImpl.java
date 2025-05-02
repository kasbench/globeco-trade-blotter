package org.kasbench.blotter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Set;

@Service
@Transactional
public class BlockServiceImpl implements BlockService {
    private final BlockRepository repository;
    private final OrderService orderService;
    private final BlockAllocationService blockAllocationService;

    @Autowired
    public BlockServiceImpl(BlockRepository repository, OrderService orderService, BlockAllocationService blockAllocationService) {
        this.repository = repository;
        this.orderService = orderService;
        this.blockAllocationService = blockAllocationService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Block> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Block> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Block save(Block block) {
        return repository.save(block);
    }

    @Override
    public Block update(Integer id, Block block) {
        Block existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Block not found with id: " + id));
        existing.setSecurity(block.getSecurity());
        existing.setOrderType(block.getOrderType());
        // version is handled by @Version
        return repository.save(existing);
    }

    @Override
    public void delete(Integer id, Integer version) {
        Block existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Block not found with id: " + id));
        if (!version.equals(existing.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(Block.class, id);
        }
        repository.delete(existing);
    }

    @Override
    public Block createBlockWithAllocations(List<Integer> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            throw new IllegalArgumentException("Order ID list must not be empty");
        }
        // Fetch all orders
        List<Order> orders = orderIds.stream()
                .map(id -> orderService.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found: " + id)))
                .collect(Collectors.toList());
        // Check all orders have same security and order type
        Set<Integer> securityIds = orders.stream().map(o -> o.getSecurity().getId()).collect(Collectors.toSet());
        Set<Integer> orderTypeIds = orders.stream().map(o -> o.getOrderType().getId()).collect(Collectors.toSet());
        if (securityIds.size() != 1 || orderTypeIds.size() != 1) {
            throw new IllegalArgumentException("All orders must have the same securityId and orderTypeId");
        }
        // Create block
        Block block = new Block();
        block.setSecurity(orders.get(0).getSecurity());
        block.setOrderType(orders.get(0).getOrderType());
        Block savedBlock = repository.save(block);
        // Create block allocations
        for (Order order : orders) {
            BlockAllocation allocation = new BlockAllocation();
            allocation.setOrder(order);
            allocation.setBlock(savedBlock);
            allocation.setQuantity(order.getQuantity());
            allocation.setFilledQuantity(java.math.BigDecimal.ZERO);
            blockAllocationService.save(allocation);
        }
        return savedBlock;
    }
} 