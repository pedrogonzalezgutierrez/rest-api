\c dev_customer
select * from pg_indexes where tablename = 'data';

\c customer
select * from pg_indexes where tablename = 'customer_customer';