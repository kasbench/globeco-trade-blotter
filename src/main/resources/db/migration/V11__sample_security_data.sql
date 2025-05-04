-- Sample data for security table
-- Author: Noah Krieger <noah@kasbench.org>
-- License: Apache 2.0

INSERT INTO public.security (ticker, description, security_type_id, version) VALUES
    ('IBM', 'IBM', 1, 1),
    ('INTC', 'Intel', 1, 1),
    ('VTI', 'Vanguard Total Stock Market Index', 12, 1),
    ('CAD', 'Canadian Dollar', 13, 1),
    ('BTC', 'Bitcoin', 10, 1),
    ('GM250502C00030000', 'GM Call Option May 2025 30.000', 8, 1); 