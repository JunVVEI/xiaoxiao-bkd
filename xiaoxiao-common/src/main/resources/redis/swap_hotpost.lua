redis.call('del', KEYS[1])

for i = 1, #ARGV, 2 do
    redis.call('zadd', KEYS[1], ARGV[i], ARGV[i+1])
end
