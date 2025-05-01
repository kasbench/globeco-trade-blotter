-- Sample data for trade_type table
-- Author: Noah Krieger <noah@kasbench.org>
-- License: Apache 2.0

INSERT INTO public.trade_type (id, abbreviation, description, version) VALUES
    (1, 'buy', 'buy', 1),
    (2, 'sell', 'sell', 1),
    (3, 'open', 'sell to open', 1),
    (4, 'close', 'buy to close', 1),
    (5, 'exercise', 'excercise option', 1),
    (6, 'sub', 'subscribe', 1),
    (7, 'red', 'redeem', 1); 