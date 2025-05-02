-- Sample data for order_status table
-- Author: Noah Krieger <noah@kasbench.org>
-- License: Apache 2.0

INSERT INTO public.order_status (id, abbreviation, description, version) VALUES
    (1, 'new', 'Newly created order', 1),
    (2, 'open', 'Order is pending', 1),
    (3, 'block', 'Order has been blocked', 1),
    (4, 'sent', 'Order has been sent', 1),
    (5, 'filled', 'Order has been filled', 1),
    (6, 'cancel', 'Order has been cancelled', 1); 