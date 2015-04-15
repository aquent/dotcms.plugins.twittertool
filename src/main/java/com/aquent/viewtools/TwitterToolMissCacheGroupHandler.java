package com.aquent.viewtools;

import com.dotmarketing.business.CacheLocator;
import com.dotmarketing.business.DotCacheAdministrator;
import com.dotmarketing.business.DotCacheException;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;

public enum TwitterToolMissCacheGroupHandler {
	// Makes this a singleton
	INSTANCE; 
	
	// A name for this cache pool
	public static final String CACHE_GROUP_NAME = "AquentGlobalConfigCache";
	
	
	/**
	 * Returns the value of a key in the miss cache or false if not found
	 * 
	 * @param 	key		The key you want to pull from cache
	 * @return			The value of the key in the properties or false if not found
	 */
	public Boolean get(String key) {
		if (!UtilMethods.isSet(key)) {
			throw new IllegalArgumentException("key cannot be null.");
		}
		
		DotCacheAdministrator cache = CacheLocator.getCacheAdministrator();
		Object o = null;

		// First we try to get the item from the cache
		try {
			o = cache.get(key, CACHE_GROUP_NAME);
		} catch (DotCacheException e) {
			Logger.error(this.getClass(), String.format("DotCacheException for Group '%s', key '%s', message: %s", CACHE_GROUP_NAME, key, e.getMessage()), e);
		}
		
		if(o == null) {
			return false;
		} else {
			return (Boolean) o;
		}
	}
	
	/**
	 * Puts an item into the cache
	 * 
	 * @param 	key		The key value
	 * @param 	value	The value
	 */
	public void put(String key, boolean value) {
		if (!UtilMethods.isSet(key)) {
			throw new IllegalArgumentException("key cannot be null.");
		}
		if (!UtilMethods.isSet(value)) {
			throw new IllegalArgumentException("value cannot be null.");
		}
		
		DotCacheAdministrator cache = CacheLocator.getCacheAdministrator();
		cache.put(key, value, CACHE_GROUP_NAME);
	}
	
	/**
	 * Removes an item from the cache
	 * 
	 * @param 	key		The key to remove
	 */
	public void remove(String key) {
		if (!UtilMethods.isSet(key)) {
			throw new IllegalArgumentException("key cannot be null.");
		}
		
		DotCacheAdministrator cache = CacheLocator.getCacheAdministrator();
		cache.remove(key, CACHE_GROUP_NAME);
	}
	
	/**
	 * Flushing the cache
	 */
	public void removeAll() {
		DotCacheAdministrator cache = CacheLocator.getCacheAdministrator();
		cache.flushGroup(CACHE_GROUP_NAME);
	}

}
