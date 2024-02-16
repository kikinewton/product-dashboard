-- Order Fill Rate is a key performance indicator (KPI) that measures the percentage of2 orders successfully 
-- fulfilled compared to the total number of2 orders received within a specific time frame. 
-- It helps businesses assess their ability to meet customer demand and fulfill orders accurately and efficiently
-- Order Fill Rate = (Total number of orders received) / (Total number of orders fulfilled) * 100


create or replace view order_fill_rate_view as
with order_details as (
    select
        po.customer_id,
        pood.product_order_id,
        od.product_id,
        p."name" as product,
        sum(od.quantity) as total_ordered
    from
        product_order_order_detail pood
    join
        order_detail od on pood.order_detail_id = od.id
    join
        product_order po on pood.product_order_id = po.id
    join
        product p on od.product_id = p.id
    where
        po.status = 'PENDING' -- Consider only pending orders
    group by
        po.customer_id, pood.product_order_id, od.product_id, p."name"
),
fulfillment_summary as (
    select
        po.customer_id,
        of2.product_order_id,
        of2.product_id,
        sum(of2.quantity) as total_fulfilled
    from
        order_fulfillment of2
    join
        product_order po on of2.product_order_id = po.id
    where
        po.status = 'PENDING' -- Consider only pending orders
    group by
        po.customer_id, of2.product_order_id, of2.product_id
)
select
    count(fs.product_order_id) as total_orders_fulfilled,
    count(od.product_order_id) as total_orders_received,
    (count(fs.product_order_id)::float / NULLIF(COUNT(od.product_order_id), 0)) * 100 AS order_fill_rate
FROM
    order_details od
LEFT JOIN
    fulfillment_summary fs ON od.customer_id = fs.customer_id AND od.product_order_id = fs.product_order_id AND od.product_id = fs.product_id;
