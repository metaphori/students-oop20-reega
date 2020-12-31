select
    c."id" as contract_id,
    c."user_id",
    c."address",
    c."services",
    c."start_time",
    c."price_model_id",
    pm."name" as price_model_name,
    pm."prices"
from
    contracts c
    inner join price_models pm on c."price_model_id" = pm."id"
where
    c."user_id" = %d;