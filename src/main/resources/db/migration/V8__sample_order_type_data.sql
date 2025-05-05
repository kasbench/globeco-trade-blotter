-- Sample data for order_type table
-- Author: Noah Krieger <noah@kasbench.org>
-- License: Apache 2.0

INSERT INTO public.order_type (id, abbreviation, description, version) VALUES
    (1, 'buy', 'Buy', 1),
    (2, 'sell', 'Sell', 1),
    (3, 'open', 'Sell to Open', 1),
    (4, 'close', 'Buy to Close', 1),
    (5, 'exercise', 'Excercise Option', 1),
    (6, 'sub', 'Subscribe', 1),
    (7, 'red', 'Redeem', 1); 