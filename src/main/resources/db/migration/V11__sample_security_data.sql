-- Sample data for security table
-- Author: Noah Krieger <noah@kasbench.org>
-- License: Apache 2.0

INSERT INTO public.security (id, ticker, description, security_type_id, version) VALUES
    (1, 'IBM', 'IBM', 1, 1),
    (2, 'INTC', 'Intel', 1, 1),
    (3, 'VTI', 'Vanguard Total Stock Market Index', 12, 1),
    (4, 'CAD', 'Canadian Dollar', 13, 1),
    (5, 'BTC', 'Bitcoin', 10, 1),
    (6, 'GM250502C00030000', 'GM Call Option May 2025 30.000', 8, 1); 