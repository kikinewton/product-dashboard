-- Order Processing Time refers to the duration it takes for an order to be processed from the moment it is
-- received until it is ready for shipment or delivery. This metric is crucial for evaluating the efficiency of
-- order fulfillment operations and ensuring timely delivery to customers

CREATE OR REPLACE VIEW order_processing_time_view AS
WITH order_processing_time AS (
    SELECT
        po.id AS product_order_id,
        MIN(po.order_fulfillment_date) AS fulfillment_start_time
    FROM
        product_order po
    WHERE
        po.status = 'COMPLETED' -- Consider only pending orders
    GROUP BY
        po.id
)
SELECT
    product_order_id,
    EXTRACT(EPOCH FROM (fulfillment_start_time - created_at)) / 3600 AS order_processing_hours
FROM
    order_processing_time
JOIN
    product_order po ON order_processing_time.product_order_id = po.id;
