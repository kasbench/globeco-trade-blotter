-- GlobeCo Trade Blotter initial schema migration
-- Author: Noah Krieger <noah@kasbench.org>
-- License: Apache 2.0

SET search_path TO public;

CREATE SEQUENCE security_type_sequence START WITH 100;

CREATE SEQUENCE order_status_sequence START WITH 100;

CREATE SEQUENCE trade_type_sequence START WITH 100;

CREATE SEQUENCE destination_sequence START WITH 100;

CREATE SEQUENCE order_type_sequence START WITH 100;



CREATE TABLE public.security_type (
    id integer DEFAULT nextval('security_type_sequence'),
    abbreviation varchar(10) NOT NULL,
    description varchar(100) NOT NULL,
    version integer NOT NULL DEFAULT 0,
    CONSTRAINT security_type_pk PRIMARY KEY (id)
);

CREATE TABLE public.blotter (
    id serial NOT NULL,
    name varchar(60) NOT NULL,
    auto_populate bit NOT NULL DEFAULT 0::bit,
    security_type_id integer,
    version integer NOT NULL DEFAULT 0,
    CONSTRAINT blotter_pk PRIMARY KEY (id)
);

CREATE TABLE public.security (
    id serial NOT NULL,
    ticker varchar(50) NOT NULL,
    description varchar(200),
    security_type_id integer NOT NULL,
    version integer NOT NULL DEFAULT 0,
    CONSTRAINT security_pk PRIMARY KEY (id)
);

CREATE TABLE public.order_type (
    id integer DEFAULT nextval('order_type_sequence') NOT NULL,
    abbreviation varchar(10) NOT NULL,
    description varchar(60) NOT NULL,
    version integer NOT NULL DEFAULT 0,
    CONSTRAINT order_type_pk PRIMARY KEY (id)
);

CREATE TABLE public.order_status (
    id integer DEFAULT nextval('order_status_sequence') NOT NULL,
    abbreviation varchar(20) NOT NULL,
    description varchar(60) NOT NULL,
    version integer NOT NULL DEFAULT 0,
    CONSTRAINT order_status_pk PRIMARY KEY (id)
);

CREATE TABLE public."order" (
    id serial NOT NULL,
    blotter_id integer,
    security_id integer NOT NULL,
    quantity decimal(18,8),
    order_timestamp timestamptz,
    version integer NOT NULL DEFAULT 0,
    order_type_id integer NOT NULL,
    order_status_id integer,
    CONSTRAINT order_pk PRIMARY KEY (id)
);

CREATE TABLE public.block (
    id serial NOT NULL,
    security_id integer NOT NULL,
    order_type_id integer NOT NULL,
    version integer NOT NULL DEFAULT 0,
    CONSTRAINT block_pk PRIMARY KEY (id)
);

CREATE TABLE public.destination (
    id integer DEFAULT nextval('destination_sequence') NOT NULL,
    abbreviation varchar(20) NOT NULL,
    description varchar(100) NOT NULL,
    version integer NOT NULL DEFAULT 0,
    CONSTRAINT destination_pk PRIMARY KEY (id)
);

CREATE TABLE public.trade_type (
    id integer DEFAULT nextval('trade_type_sequence') NOT NULL,
    abbreviation varchar(10) NOT NULL,
    description varchar(60) NOT NULL,
    version integer NOT NULL DEFAULT 0,
    CONSTRAINT trade_type_pk PRIMARY KEY (id)
);

CREATE TABLE public.trade (
    id serial NOT NULL,
    destination_id integer NOT NULL,
    block_id integer NOT NULL,
    quantity decimal(18,8),
    trade_type_id integer NOT NULL,
    filled_quantity decimal(18,8) NOT NULL DEFAULT 0,
    version integer NOT NULL DEFAULT 0,
    CONSTRAINT trade_pk PRIMARY KEY (id)
);

CREATE TABLE public.block_allocation (
    id serial NOT NULL,
    order_id integer NOT NULL,
    block_id integer NOT NULL,
    quantity decimal(18,8) NOT NULL,
    filled_quantity decimal(18,8) NOT NULL DEFAULT 0,
    version integer NOT NULL DEFAULT 0,
    CONSTRAINT block_allocation_pk PRIMARY KEY (id)
);

ALTER TABLE public.security
    ADD CONSTRAINT security_type_security_fk FOREIGN KEY (security_type_id)
    REFERENCES public.security_type (id) MATCH FULL
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE public.blotter
    ADD CONSTRAINT security_type_blotter_fk FOREIGN KEY (security_type_id)
    REFERENCES public.security_type (id) MATCH FULL
    ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE public."order"
    ADD CONSTRAINT blotter_order_fk FOREIGN KEY (blotter_id)
    REFERENCES public.blotter (id) MATCH FULL
    ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE public."order"
    ADD CONSTRAINT security_fk FOREIGN KEY (security_id)
    REFERENCES public.security (id) MATCH FULL
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE public."order"
    ADD CONSTRAINT order_type_order_fk FOREIGN KEY (order_type_id)
    REFERENCES public.order_type (id) MATCH FULL
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE public."order"
    ADD CONSTRAINT order_status_order_fk FOREIGN KEY (order_status_id)
    REFERENCES public.order_status (id) MATCH FULL
    ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE public.block
    ADD CONSTRAINT security_block_fk FOREIGN KEY (security_id)
    REFERENCES public.security (id) MATCH FULL
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE public.block
    ADD CONSTRAINT order_type_block_fk FOREIGN KEY (order_type_id)
    REFERENCES public.order_type (id) MATCH FULL
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE public.trade
    ADD CONSTRAINT destination_trade_fk FOREIGN KEY (destination_id)
    REFERENCES public.destination (id) MATCH FULL
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE public.trade
    ADD CONSTRAINT block_trade_fk FOREIGN KEY (block_id)
    REFERENCES public.block (id) MATCH FULL
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE public.trade
    ADD CONSTRAINT trade_type_trade_fk FOREIGN KEY (trade_type_id)
    REFERENCES public.trade_type (id) MATCH FULL
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE public.block_allocation
    ADD CONSTRAINT order_block_allocation_fk FOREIGN KEY (order_id)
    REFERENCES public."order" (id) MATCH FULL
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE public.block_allocation
    ADD CONSTRAINT block_block_allocation_fk FOREIGN KEY (block_id)
    REFERENCES public.block (id) MATCH FULL
    ON DELETE RESTRICT ON UPDATE CASCADE; 