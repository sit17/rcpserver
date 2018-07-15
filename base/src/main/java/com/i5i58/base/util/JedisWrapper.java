package com.i5i58.base.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

public class JedisWrapper {
	private static JedisPool jedisPool;
	private static Logger logger = Logger.getLogger("JedisUtils");

//	private static int database;
//	private static String host;
//	private static int port;
//	private static int maxidle;
//	private static int minidle;
//	private static int maxactive;
//	private static int maxwait;
//	private static String password;
//	private static int timeout;
	private static int RETRY_TIMES = 5;

	public static void init() {
		int database = Integer.parseInt(PropertiesUtil.getProperty("my.jedis.database", ""));
		String host = PropertiesUtil.getProperty("my.jedis.host", "localhost");
		int port = Integer.parseInt(PropertiesUtil.getProperty("my.jedis.port", ""));
		int maxidle = Integer.parseInt(PropertiesUtil.getProperty("my.jedis.maxidle", ""));
		int minidle = Integer.parseInt(PropertiesUtil.getProperty("my.jedis.minidle", ""));
		int maxactive = Integer.parseInt(PropertiesUtil.getProperty("my.jedis.maxactive", ""));
		int maxwait = Integer.parseInt(PropertiesUtil.getProperty("my.jedis.maxwait", ""));
		String password = PropertiesUtil.getProperty("my.jedis.password", "");
		int timeout = Integer.parseInt(PropertiesUtil.getProperty("my.jedis.timeout", ""));
		
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(maxactive);
		config.setMaxIdle(maxidle);
		config.setMaxWaitMillis(maxwait);
		config.setMinIdle(minidle);
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);
		config.setTestOnCreate(true);
		// config.setTestOnBorrow(Global.TEST_ON_BORROW);
		// config.setTestOnReturn(Global.TEST_ON_RETURN);
		// jedisPool = new
		// JedisPool("redis://:"+Global.REDIS_SERVER_PASSWORD+"@"+Global.REDIS_SERVER_URL+":"+Global.REDIS_SERVER_PORT);
		if (jedisPool != null) {
			if (!jedisPool.isClosed()) {
				jedisPool.close();
			}
			jedisPool = null;
		}
		if (password != null && !password.isEmpty()) {
			jedisPool = new JedisPool(config, host, port, timeout, password, database);// ,
		} else {
			jedisPool = new JedisPool(config, host, port, timeout);// ,
		}
	}

	private JedisPool getJedisPool() {
		return jedisPool;
	}

	private Jedis getJedis() throws IOException {
		Jedis jedis;
		int count = 0;
		do {
			jedis = getJedisPool().getResource();
		} while (jedis == null && count++ < JedisWrapper.RETRY_TIMES);
		if (jedis == null) {
			throw new IOException("");
		}
		return jedis;
	}

	/**
	 * Handle jedisException, write log and return whether the connection is
	 * broken.
	 */
	protected boolean handleJedisException(Exception jedisException) {
		if (jedisException instanceof JedisConnectionException) {
			logger.error("Redis connection " + " lost.", jedisException);
		} else if (jedisException instanceof JedisDataException) {
			if ((jedisException.getMessage() != null) && (jedisException.getMessage().indexOf("READONLY") != -1)) {
				logger.error("Redis connection " + " are read-only slave.", jedisException);
			} else {
				// dataException, isBroken=false
				return false;
			}
		} else {
			logger.error("Jedis exception happen.", jedisException);
		}
		return true;
	}

	/**
	 * Return jedis connection to the pool, call different return methods
	 * depends on the conectionBroken status.
	 */
	protected void closeResource(Jedis jedis, boolean conectionBroken) {
		try {
			if (conectionBroken) {
				jedis.close();
//				jedisPool.returnBrokenResource(jedis);
			} else {
//				jedisPool.returnResource(jedis);
				jedis.close();
			}
		} catch (Exception e) {
			logger.error("return back jedis failed, will fore close the jedis.", e);
			jedis.close();
		}
	}

	public String set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			logger.error("Jedis set key = " + key + ", value = " + value, e);
			return "";
		}
		String result = jedis.set(key, value);
		jedis.close();
		return result;
	}
	
	public String set(String key, String value,final String nxxx, final String expx,
		      final long time) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			logger.error("Jedis set key = " + key + ", value = " + value, e);
			return "";
		}
		String result = jedis.set(key, value, nxxx, expx, time);
		jedis.close();
		return result;
	}

	public String get(String key) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			logger.error("Jedis get key = " + key, e);
			return "";
		}
		String result = jedis.get(key);
		jedis.close();
		return result;
	}

	public boolean exist(String key) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			logger.error("Jedis exist key = " + key, e);
			return false;
		}
		boolean ret = jedis.exists(key);
		jedis.close();
		return ret;
	}

	public boolean saddByNewOne(String key, String... members) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis saddByNewOne key = " + key + ", members = ";
			for (int i = 0; i < members.length; ++i) {
				message = message + ", " + members[i];
			}
			logger.error(message, e);
			return false;
		}
		long ret = jedis.sadd(key, members);
		jedis.close();
		if (ret == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean sismember(final String key, final String member) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis sismember key = " + key + ", member = " + member;
			logger.error(message, e);
			return false;
		}
		Boolean ret = jedis.sismember(key, member);
		jedis.close();
		return ret;
	}

	public boolean hexist(String key, String field) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis hexist key = " + key + ", field = " + field;
			logger.error(message, e);
			return false;
		}
		boolean ret = jedis.hexists(key, field);
		jedis.close();
		return ret;
	}

	public Long hset(String key, String item, String value) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis hset key = " + key + ", item = " + item + ", value = " + value;
			logger.error(message, e);
			return 0L;
		}
		Long result = jedis.hset(key, item, value);
		jedis.close();
		return result;
	}

	public String hget(String key, String item) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis hget key = " + key + ", item = " + item;
			logger.error(message, e);
			return "";
		}
		String result = jedis.hget(key, item);
		jedis.close();
		return result;
	}

	public Map<String, String> hgetAll(String key) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis hgetAll key = " + key;
			logger.error(message, e);
			return new HashMap<String, String>();
		}
		Map<String, String> result = jedis.hgetAll(key);
		jedis.close();
		return result;
	}

	public String hmset(String key, Map<String, String> hash){
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis hgetAll key = " + key;
			logger.error(message, e);
			return "";
		}		
		String ret = jedis.hmset(key, hash);
		jedis.close();
		return ret;
	}
	/**
	 * Redis Hmget 命令用于返回哈希表中，一个或多个给定字段的值。 如果指定的字段不存在于哈希表，那么返回一个 nil 值。
	 * 
	 * @param key
	 * @param item
	 * @return 一个包含多个给定字段关联值的表，表值的排列顺序和指定字段的请求顺序一样。
	 * @throws IOException
	 */
	public List<String> hmget(String key, String... item) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis hmget key = " + key;
			logger.error(message, e);
			return new ArrayList<String>();
		}
		List<String> result = jedis.hmget(key, item);
		jedis.close();
		return result;
	}

	public Long incr(String key) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis incr key = " + key;
			logger.error(message, e);
			return 0L;
		}
		Long result = jedis.incr(key);
		jedis.close();
		return result;
	}

	public Long decr(String key) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis decr key = " + key;
			logger.error(message, e);
			return 0L;
		}
		Long result = jedis.decr(key);
		jedis.close();
		return result;
	}

	public Long expire(String key, int second) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis expire key = " + key + ", second = " + second;
			logger.error(message, e);
			return 0L;
		}
		Long result = jedis.expire(key, second);
		jedis.close();
		return result;
	}

	public Long ttl(String key) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis ttl key = " + key;
			logger.error(message, e);
			return 0L;
		}
		Long result = jedis.ttl(key);
		jedis.close();
		return result;
	}

	public Long hdel(String key, String item) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis hdel key = " + key + ", item = " + item;
			logger.error(message, e);
			return 0L;
		}
		Long result = jedis.hdel(key, item);
		jedis.close();
		return result;
	}

	public Long del(String key) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis hdel key = " + key;
			logger.error(message, e);
			return 0L;
		}
		Long result = jedis.del(key);
		jedis.close();
		return result;
	}

	public Long rpush(String key, String... strings) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis rpush key = " + key + ", strings = " + strings;
			for (int i = 0; i < strings.length; ++i) {
				message = message + ", " + strings[i];
			}
			logger.error(message, e);
			return 0L;
		}
		Long result = jedis.rpush(key, strings);
		jedis.close();
		return result;
	}

	/**
	 * Redis Lrange 返回列表中指定区间内的元素，区间以偏移量 START 和 END 指定。 其中 0 表示列表的第一个元素， 1
	 * 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	 * 
	 * @param string
	 * @param start
	 * @param end
	 * @return
	 * @throws IOException
	 */
	public List<String> lrange(String key, int start, int end) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis lrange key = " + key + ", start = " + start + ", end = " + end;
			logger.error(message, e);
			return new ArrayList<String>();
		}
		List<String> result = jedis.lrange(key, start, end);
		jedis.close();
		return result;
	}

	/**
	 * 从列表中从头部开始移除count个匹配的值。如果count为零，所有匹配的元素都被删除。如果count是负数，内容从尾部开始删除。
	 * 
	 * @param string
	 * @param string2
	 * @param i
	 * @throws IOException
	 */
	public Long lrem(String key, Long count, String value) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis lrem key = " + key + ", count = " + count + ", value = " + value;
			logger.error(message, e);
			return 0L;
		}
		Long result = jedis.lrem(key, count, value);
		jedis.close();
		return result;
	}

	/**
	 * Redis Zadd 命令用于将一个或多个成员元素及其分数值加入到有序集当中。
	 * 如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。
	 * 分数值可以是整数值或双精度浮点数。 如果有序集合 key 不存在，则创建一个空的有序集并执行 ZADD 操作。 当 key
	 * 存在但不是有序集类型时，返回一个错误。
	 * 
	 * @param string
	 * @param i
	 * @param string2
	 * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
	 * @throws IOException
	 */
	public Long zadd(String key, double score, String member) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis zadd key = " + key + ", score = " + score + ", member = " + member;
			logger.error(message, e);
			return 0L;
		}
		Long result = jedis.zadd(key, score, member);
		jedis.close();
		return result;
	}

	public Long zadd(String key, double score, String member, ZAddParams params) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis zadd key = " + key + ", score = " + score + ", member = " + member + ", params = "
					+ params;
			logger.error(message, e);
			return 0L;
		}
		Long result = jedis.zadd(key, score, member, params);
		jedis.close();
		return result;
	}

	/**
	 * Redis Zrevrangebyscore 返回有序集中指定分数区间内的所有的成员。有序集成员按分数值递减(从大到小)的次序排列。
	 * 具有相同分数值的成员按字典序的逆序(reverse lexicographical order )排列。 除了成员按分数值递减的次序排列这一点外，
	 * ZREVRANGEBYSCORE 命令的其他方面和 ZRANGEBYSCORE 命令一样。
	 * 
	 * @param key
	 * @param max
	 * @param min
	 * @param offset
	 * @param count
	 * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
	 * @throws IOException
	 */
	public Set<String> zrevrangebyscore(String key, String max, String min, int offset, int count) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis zrevrangebyscore key = " + key + ", max = " + max + ", min = " + min + ", offset = "
					+ offset + " , count = " + count;
			logger.error(message, e);
			return new HashSet<String>();
		}
		Set<String> result = jedis.zrevrangeByScore(key, max, min, offset, count);
		jedis.close();
		return result;
	}

	/**
	 * 
	 * Redis zcount 返回有序集 key 中， score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员的数量。
	 * 关于参数 min 和 max 的详细使用方法，请参考 ZRANGEBYSCORE 命令
	 * @author frank
	 * @param key
	 * @param max
	 * @param min
	 * @return
	 */
	public long zcount(String key, double min, double max) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis zcount key = " + key + ", max = " + max + ", min = " + min;
			logger.error(message, e);
			return 0L;
		}
		long result = jedis.zcount(key, min, max);
		jedis.close();
		return result;
	}

	/**
	 * 在有序集合增加成员的分数
	 * 
	 * @param key
	 * @param score
	 * @param member
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public double zincrby(String key, double score, String member, ZIncrByParams params) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis zincrby key = " + key + ", score = " + score + ", member = " + member
					+ ", params = " + params;
			logger.error(message, e);
			return 0.0;
		}
		double result = jedis.zincrby(key, score, member, params);
		jedis.close();
		return result;
	}

	/**
	 * 删除有序集合成员，如果成员不在集合中，不执行任何操作
	 * 
	 * @param key
	 * @param members
	 * @return
	 * @throws IOException
	 */
	public long zrem(String key, String... members) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis zrem key = " + key + ", members = " + members;
			for (int i = 0; i < members.length; ++i) {
				message = message + ", " + members[i];
			}
			logger.error(message, e);
			return 0L;
		}
		long result = jedis.zrem(key, members);
		jedis.close();
		return result;
	}

	public Double zscore(String key, String members) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis zscore key = " + key + ", members = " + members;
			logger.error(message, e);
			return 0.0;
		}
		Double result = jedis.zscore(key, members);
		jedis.close();
		return result;
	}

	/**
	 * 在键的排序集合中返回排名（或指数）或成员，分数由低到高排列。当给定成员不存在于已排序的集合中时，返回的特殊值“无”。返回的排名（或指数）的成员都是0命令。
	 * 
	 * @param key
	 * @param member
	 * @return
	 * @throws IOException
	 */
	public long zrank(String key, String member) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis zrank key = " + key + ", member = " + member;
			logger.error(message, e);
			return 0;
		}
		long result = jedis.zrank(key, member);
		jedis.close();
		return result;
	}

	public long srem(String key, String item) {
		Jedis jedis;
		try {
			jedis = getJedis();
		} catch (IOException e) {
			String message = "Jedis srem key = " + key + ", item = " + item;
			logger.error(message, e);
			return 0;
		}
		long result = jedis.srem(key, item);
		jedis.close();
		return result;
	}
}