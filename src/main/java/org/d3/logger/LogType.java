package org.d3.logger;

/**
 * 日志类型
 * 
 * @author Hyint
 */
public enum LogType {
 
	/**
	 * 登录日志
	 * 
	 * <pre>
	 * 格式: 
	 * time:操作的服务器时间戳, source: 日志来源,   	 ip:客户机的IP, 		    hp:角色的当前HP量, 		mp:角色的当前MP量, 
	 * gas :角色的当前Gas量,   exp:角色的当前经验值,	 hpBag:角色的HP便携包值,  mpBag:角色的MP便携包值, 	level:角色的等级,
	 * hpMax:角色的HPMax值, 	 mpMax:角色的MPMax值, gasMax: 角色的GasMax值,  silver: 角色的游戏币剩余量,golden: 角色金币剩余量, 
	 * playerId: 角色的ID,    playerName: 角色名,  branching: 角色分线, fightMode: 战斗模式.(0-和平,1-杀戮), loginDays: 连续登录天数,
	 * loginTime:角色登录日期, logoutTime: 角色登出时间, loginCount: 登录次数, onlineTimes: 角色当前在线时间, 
	 * totalLoginDays:角色总共登录天数, totalOnlineTimes:角色总共在线时间
	 * </pre>
	 */
	LOGIN("LOGIN"),

	/** 
	 * 游戏币日志
	 */
	SILVER("SILVER"),
	
	/** 
	 * 	元宝日志.
	 * 
	 * 	日志格式: 
	 */
	GOLDEN("GOLDEN"),
	
	/** 
	 * 	礼金日志.
	 * 
	 * 	日志格式: 
	 */
	COUPON("COUPON"),
	
	/** 
	 * 主支线任务日志
	 * 
	 * <pre>
	 * 日志格式: 
	 * time:操作的服务器时间戳, source: 日志来源, playerId:任务拥有者, playerName:拥有者的名字, taskId: 任务ID, taskName:任务名,
	 * info:收支情况_物品类型_物品自增ID_物品基础ID_物品数量_是否自动购买(0-不自动购买, 1-自动购买)|...
	 * </pre>
	 */
	TASK("TASK"),
	
	/** 
	 * 物品日志
	 * <pre>
	 * 日志格式: 
	 * time:操作的服务器时间戳, source: 日志来源, playerId:任务拥有者, playerName:拥有者的名字, 
	 * </pre>
	 */
	GOODS("GOODS"),
	
	/** 
	 * 坐骑日志
	 */
	HORSE("HORSE"),
	
	/**
	 * 帮派日志
	 */
	ALLIANCE("ALLIANCE"),
	
	/**
	 * 家将日志
	 * */
	PET("PET"),
	
	/**
	 * 经脉日志
	 * 
	 * 格式: 
	 * <pre>
	 * time:当前服务器时间, source:日志来源类型, playerId:角色ID, isSucceed:点脉是否成功,
	 * attrKey:点脉成功加成, attrValue:点脉成功加成值,info:收支情况_物品类型_物品自增ID_物品基础ID_物品数量|...
	 * </pre>
	 */
	MERIDIAN("MERIDIAN"),
	
	/**
	 * 肉身日志
	 * 
	 * 格式:
	 * <pre>
	 * time:当前服务器时间, playerId:角色ID, mortalType:肉身类型, mortalLevel:肉身等级
	 * isSucceed:升级成功?, goodsInfo:收支情况_物品类型_物品自增ID_物品基础ID_物品数量|...
	 * </pre>
	 */
	MORTAL("MORTAL"),
	
	/**
	 * 角色经验日志
	 * 
	 * 格式:
	 * <pre>
	 * time:当前服务器时间, playerId:角色ID, playerName:角色名,source:经验来源, monsterName:怪物名称, addExp:增加经验, 
	 * exp:当前经验, taskType: 任务类型, taskName:任务名称,playerLevel:获得经验后的等级, itemId: 道具ID, itemName:道具名, 
	 * userName:角色帐号, activeName:活动名称
	 * </pre>
	 */
	PLAYER_EXP("PLAYER_EXP"),
	
	/**
	 * 角色等级日志
	 * 
	 * 格式:
	 * <pre>
	 * time:当前服务器时间, playerId:角色ID,....
	 * </pre>
	 */
	PLAYER_LEVEL("PLAYER_LEVEL"),
	
	
	/**
	 * 玩家交易日志
	 * 
	 * 格式:
	 * <pre>
	 * time:当前服务器时间, playerId:角色ID, targetId:交易目标ID,
	 * info:
	 * </pre>
	 */
	PLAYER_TRADE("PLAYER_TRADE"),
	
	
	/**
	 * 角色注册统计日志
	 * 
	 * 格式:
	 * <pre>
	 * time:当前服务器时间, 
	 * info:
	 * </pre>
	 */
	PLAYER_REGISTER_STATISTIC("PLAYER_REGISTER_STATISTIC"),
	
	
	/**
	 * 掉落日志
	 * 
	 * 格式:
	 * <pre>
	 * time:当前服务器时间,source:来源, moneyOrient:货币的收支,playerId:拾取的角色ID,playerName:拾取的角色的名字,userName:账号,golden:拾取的金币数量,silver:拾取的游戏币数量, monsterName:怪物名字, mapId:地图ID, info:物品详细信息
	 * </pre>
	 */
	DROP("DROP"),
	
	/**
	 * 活动
	 * 
	 */
	ACTIVE("ACTIVE"),
	
	/**
	 * 副本
	 */
	 DUNGEON("DUNGEON"),
	 
	 /**
	  * 乱武战场
	  */
	 BATTLE_FILED("BATTLE_FILED"),
	 
	 /**
	  * 背包物品移动日志
	  */
	 GOODS_MOVE("GOODS_MOVE"),
	
	;
	
	/** 日志名*/
	private String logName = "";
	
	/**
	 * 日志类型
	 * 
	 * @param logName		日志名
	 */
	LogType(String name) {
		this.logName = name;
	}

	public String getLogName() {
		return logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}
}
