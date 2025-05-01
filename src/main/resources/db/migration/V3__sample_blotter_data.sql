-- Sample data for blotter table
-- Author: Noah Krieger <noah@kasbench.org>
-- License: Apache 2.0

INSERT INTO public.blotter (id, name, auto_populate, security_type_id, version) VALUES
    (1, 'Default', B'0', NULL, 1),
    (2, 'Common Stock', B'1', 1, 1),
    (3, 'Priority', B'0', NULL, 1); 