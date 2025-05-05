-- Sample data for trade_type table
-- Author: Noah Krieger <noah@kasbench.org>
-- License: Apache 2.0

INSERT INTO public.trade_type (id, abbreviation, description, version) VALUES
    (1, 'MKT', 'Market Order', 1),
    (2, 'LMT', 'Limit Order', 1),
    (3, 'STP', 'Stop Order', 1),
    (4, 'STPLMT', 'Stop Limit Order', 1),
    (5, 'MOC', 'Market on Close', 1); 

