select
    u.*
from
    users u
    inner join authentication a on u.id = a.user_id
where
    a.selector = '%s'
    and a."validator" = '%s';