package main.java.com.buildsim.cloud;

import main.java.com.buildsim.init.WatchDogConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisShardInfo;

import java.util.HashSet;
import java.util.Set;

public class RedisAccessFactory {
	private static final Logger LOG = LoggerFactory.getLogger(RedisAccessFactory.class);
	public static RedisAccess getAccess(){
		String detect = WatchDogConfig.readProperty("platform");
		if(detect==null){
			detect = "local";
		}
		switch(detect.toLowerCase()){
			case "aws":
				LOG.info("Connecting to AWS ElastiCache");
				Set<HostAndPort> hosts = new HashSet<>();
				int hostNum = Integer.parseInt(WatchDogConfig.readProperty("RedisHostNumber"));
				for(int i=0;i<hostNum;i++){
					hosts.add(new HostAndPort(WatchDogConfig.readProperty("RedisHost"+i),
							Integer.parseInt(WatchDogConfig.readProperty("RedisPort"+i))));
				}
				JedisCluster cluster = new JedisCluster(hosts);
				return new RedisAccess(cluster);
			case "azure":
				LOG.info("Connecting to Azure Redis cluster");
				JedisShardInfo shardInfo = new JedisShardInfo(WatchDogConfig.readProperty("RedisAzureHost"),
						Integer.parseInt(WatchDogConfig.readProperty("RedisAzurePort")),
						true);
				shardInfo.setPassword(WatchDogConfig.readProperty("RedisAzurePassword"));
				Jedis azureClient = new Jedis(shardInfo);
				return new RedisAccess(azureClient);
			default:
				LOG.info("Connecting to local Redis server");
				Jedis client = new Jedis("localhost");
				return new RedisAccess(client);
		}
	}
}
