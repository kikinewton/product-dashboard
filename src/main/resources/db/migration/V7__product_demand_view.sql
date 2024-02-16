
CREATE OR REPLACE VIEW product_demand_view AS
WITH product_demand AS (
    SELECT
        od.product_id,
        p.name AS product_name,
        SUM(od.quantity) AS total_ordered
    FROM
        order_detail od
    JOIN
        product p ON od.product_id = p.id
    join product_order_order_detail pood on od.id = pood.order_detail_id
    JOIN
        product_order po ON pood.product_order_id = po.id
    WHERE
        po.status = 'COMPLETED' -- Consider only completed orders
    GROUP BY
        od.product_id, p.name
)
SELECT
    product_id,
    product_name,
    total_ordered
FROM
    product_demand
ORDER BY
    total_ordered DESC; -- Display products in descending order of total quantity ordered
