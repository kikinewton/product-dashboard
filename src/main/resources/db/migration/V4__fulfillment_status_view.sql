

CREATE OR REPLACE VIEW order_fulfillment_status AS
WITH order_details AS (
         SELECT po.customer_id,
            pood.product_order_id,
            od_1.product_id,
            p.name AS product,
            p.pack_weight_in_kg,
            sum(od_1.quantity) AS total_ordered,
            po.created_at AS order_created_at,
            po.required_date AS required_at
           FROM product_order_order_detail pood
             JOIN order_detail od_1 ON pood.order_detail_id = od_1.id
             JOIN product_order po ON pood.product_order_id = po.id
             JOIN product p ON od_1.product_id = p.id
          WHERE po.status::text = 'PENDING'::text
          GROUP BY po.customer_id, pood.product_order_id, od_1.product_id, p.name, po.created_at, po.required_date, p.pack_weight_in_kg
        ), fulfillment_summary AS (
         SELECT po.customer_id,
            of2.product_order_id,
            of2.product_id,
            sum(of2.quantity) AS total_fulfilled,
            max(of2.created_at) AS fulfillment_date
           FROM order_fulfillment of2
             JOIN product_order po ON of2.product_order_id = po.id
          WHERE po.status::text = 'PENDING'::text
          GROUP BY po.customer_id, of2.product_order_id, of2.product_id
        )
 SELECT ( SELECT c.name
           FROM customer c
          WHERE c.id = od.customer_id) AS customer,
    od.product_order_id,
    od.product_id,
    od.product,
    od.total_ordered,
    COALESCE(fs.total_fulfilled, 0::bigint) AS total_processed,
    od.pack_weight_in_kg * COALESCE(fs.total_fulfilled, 0::bigint) as total_processed_weight_kg,
    od.total_ordered - COALESCE(fs.total_fulfilled, 0::bigint) AS remaining_to_process,
    EXTRACT(day FROM max(od.required_at) - now()) AS days_to_deadline
   FROM order_details od
     LEFT JOIN fulfillment_summary fs ON od.customer_id = fs.customer_id AND od.product_order_id = fs.product_order_id AND od.product_id = fs.product_id
  GROUP BY od.customer_id, od.product_order_id, od.product_id, od.product, od.total_ordered, od.order_created_at, fs.total_fulfilled, od.pack_weight_in_kg;