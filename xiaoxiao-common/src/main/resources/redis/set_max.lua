local key = KEYS[1]
local val = redis.call("get", key)

if val == false or tonumber(val) < tonumber(ARGV[1])
then
    return redis.call('setex',KEYS[1],ARGV[2],ARGV[1])
else
    return 0
end