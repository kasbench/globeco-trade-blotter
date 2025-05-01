-- Sample data for security_type table
-- Author: Noah Krieger <noah@kasbench.org>
-- License: Apache 2.0

INSERT INTO public.security_type (id, abbreviation, description, version) VALUES
    (1, 'CS', 'Common Stock', 1),
    (2, 'PS', 'Preferred Stock', 1),
    (3, 'CB', 'Corporate Bond', 1),
    (4, 'MB', 'Muncipal Bond', 1),
    (5, 'TBond', 'Treasury Bond', 1),
    (6, 'TBill', 'Treasury Bill', 1),
    (7, 'TNote', 'Treasury Note', 1),
    (8, 'CALL', 'Call Option', 1),
    (9, 'PUT', 'Put Option', 1),
    (10, 'DA', 'Digital Asset', 1),
    (11, 'MF', 'Mutual Fund', 1),
    (12, 'ETF', 'Exchange Traded Fund', 1),
    (13, 'CUR', 'Currency', 1),
    (14, 'CP', 'Commercial Paper', 1); 