package com.aquent.viewtools;

import org.apache.velocity.tools.view.tools.ViewTool;

import com.dotmarketing.beans.Host;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;

import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Exposes Twitter4J to dotCMS in a viewtool
 * Mapped to $twitter
 * 
 * @author cfalzone
 *
 */
public class TwitterTool implements ViewTool {

	private Twitter twitter;
    private boolean inited = false;
    
    public void init(Object initData) { 
    	Logger.debug(this, "Twitter Tool Starting Up");
    	
    	// Get the default host
    	Host defaultHost;
		try {
			defaultHost = APILocator.getHostAPI().findDefaultHost(APILocator.getUserAPI().getSystemUser(), false);
		} catch (Exception e) {
			Logger.error(this, "Unable to get the default host", e);
			return;
		}
		
		Logger.info(this, "Default Host = "+defaultHost.getHostname());
		
		boolean debug = defaultHost.getBoolProperty("twitter4jDebug");
		String ck = defaultHost.getStringProperty("twitter4jConsumerKey");
		String cks = defaultHost.getStringProperty("twitter4jConsumerSecret");
		String at = defaultHost.getStringProperty("twitter4jAccessToken");
		String ats = defaultHost.getStringProperty("twitter4jTokenSecret");
		
		Logger.info(this, "Twitter Auth - CK="+ck+", CKS="+cks+", AT="+at+", ATS="+ats);
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(debug)
		  .setOAuthConsumerKey(ck)
		  .setOAuthConsumerSecret(cks)
		  .setOAuthAccessToken(at)
		  .setOAuthAccessTokenSecret(ats)
		  .setUseSSL(true);
    	
		Logger.info(this, "Twitter Configuration: "+cb);
		
        try {
            twitter = new TwitterFactory(cb.build()).getInstance(); 
        } catch (Exception e) {
            Logger.error(this, "Error getting twitter instance", e);
            return;
        }
        
        inited = true;
        Logger.info(this, "Twitter Tool Started Up");
    }
    
    /**
     * Returns the twitter object - use with care
     * 
     * @return	The twitter object
     */
    public Twitter getTwitter() {
    	if(inited) {
    		return twitter;
    	} else {
    		Logger.warn(this, "ViewTool not inited");
    		return null;
    	}
    }
 
    /**
     * Fetched the last count tweets for the Screen Name
     * 
     * See {@link twitter4j.Twitter.getUserTimeline}
     * 
     * @param screenName    The screen name to fetch the tweets for
     * @param page			The page of results to pull, if empty 1
     * @param count         The number of results to pull per page, if empty 20
     * @return              A list of the last count tweets for the the screen name, or null if something went wrong
     */
    public ResponseList<Status> getUserTimeline(String screenName, int page, int count) {
        if(inited) {
        	if(!UtilMethods.isSet(page)) page = 1;
        	if(!UtilMethods.isSet(count)) count = 20;
        	
            try {
                return twitter.getUserTimeline(screenName, new Paging(page, count));
            } catch (Exception e) {
                Logger.error(this, "Error Fetching timeline", e);
                return null;
            }
        } else {
            Logger.warn(this, "ViewTool not inited");
            return null;
        }
    }
    
    /**
     * Fetches the last count tweets for the user ID
     * 
     * See {@link twitter4j.Twitter.getUserTimeline}
     * 
     * @param userId    The User ID to fetch the tweets for
     * @param page		The page of results to pull, if empty 1
     * @param count     The number of results to pull per page, if empty 20
     * @return          A list of the last count tweets for the the user id, or null if something went wrong
     */
    public ResponseList<Status> getUserTimeline(long userId, int page, int count) {
        if(inited) {
        	if(!UtilMethods.isSet(page)) page = 1;
        	if(!UtilMethods.isSet(count)) count = 20;
        	
            try {
                return twitter.getUserTimeline(userId, new Paging(page, count));
            } catch (Exception e) {
                Logger.error(this, "Error Fetching timeline", e);
                return null;
            }
        } else {
            Logger.warn(this, "ViewTool not inited");
            return null;
        }
    }
    
    /**
     * Returns a Twitter4J User object for the screen name
     * 
     * See {@link twitter4j.Twitter.showUser}
     * 
     * @param screenName    The screen name to look for
     * @return              A Twitter4J User object for the screen name or null if something went wrong
     */
    public User showUser(String screenName) {
    	if(inited) {
    		try {
				return twitter.showUser(screenName);
			} catch (Exception e) {
				Logger.error(this, "Error Fetching user", e);
				return null;
			}
    	} else {
    		Logger.warn(this, "ViewTool not inited");
    		return null;
    	}
    }
    
    /**
     * Returns a Twitter4J User object for the userId
     * 
     * See {@link twitter4j.Twitter.showUser}
     * 
     * @param screenName    The screen name to look for
     * @return              A Twitter4J User object for the screen name or null if something went wrong
     */
    public User showUser(long userId) {
    	if(inited) {
    		try {
				return twitter.showUser(userId);
			} catch (Exception e) {
				Logger.error(this, "Error Fetching user", e);
				return null;
			}
    	} else {
    		Logger.warn(this, "ViewTool not inited");
    		return null;
    	}
    }
    
    /**
     * Gets a list of up to 20 followers for the user
     * 
     * See {@link twitter4j.Twitter.getFollowersList}
     * 
     * @param screenName   The screen name to get a list of follower ids for
     * @return             A list of up to 20 of the user's followers
     */
    public PagableResponseList<User> getFollowersList(String screenName) {
    	if(inited) {
    		try {
				return twitter.getFollowersList(screenName, -1);
			} catch (Exception e) {
				Logger.error(this, "Error Fetching user's Followers", e);
				return null;
			}
    	} else {
    		Logger.warn(this, "ViewTool not inited");
    		return null;
    	}
    }
    
    /**
     * Gets a list of up to 20 followers for the user
     * 
     * See {@link twitter4j.Twitter.getFollowersList}
     * 
     * @param userId    The userid to get a list of follower ids for
     * @return          A List of up to 20 of the user followers
     */
    public PagableResponseList<User> getFollowersList(long userId) {
    	if(inited) {
    		try {
				return twitter.getFollowersList(userId, -1);
			} catch (Exception e) {
				Logger.error(this, "Error Fetching user's Followers", e);
				return null;
			}
    	} else {
    		Logger.warn(this, "ViewTool not inited");
    		return null;
    	}
    }
    
    /**
     * Gets a list of up to 20 members for the user's list
     * 
     * See {@link twitter4j.Twitter.getUserListMembers}
     * 
     * @param ownerScreenName    The list owner's screen name
     * @param slug				 The list's slug
     * @return					 A list of up to 20 of the list's members
     */
    public PagableResponseList<User> getUserListMembers(String ownerScreenName, String slug) {
    	if(inited) {
    		try {
				return twitter.getUserListMembers(ownerScreenName, slug, -1);
			} catch (Exception e) {
				Logger.error(this, "Error Fetching user's Followers", e);
				return null;
			}
    	} else {
    		Logger.warn(this, "ViewTool not inited");
    		return null;
    	}
    }
    
    /**
     * Gets a list of up to 20 members for the user's list
     * 
     * See {@link twitter4j.Twitter.getUserListMembers}
     * 
     * @param ownerId   The list owner's id
     * @param slug		The list's slug
     * @return			A list of up to 20 of the list's members
     */
    public PagableResponseList<User> getUserListMembers(long ownerId, String slug) {
    	if(inited) {
    		try {
				return twitter.getUserListMembers(ownerId, slug, -1);
			} catch (Exception e) {
				Logger.error(this, "Error Fetching user's Followers", e);
				return null;
			}
    	} else {
    		Logger.warn(this, "ViewTool not inited");
    		return null;
    	}
    }
    
    /**
     * Gets a list of tweets from a user's list 
     * 
     * See {@link twitter4j.Twitter.getUserListStatuses}
     * 
     * @param ownerScreenName    The list owner's screen name
     * @param slug               The list's slug
     * @param page               The page to pull
     * @param count              The number of items to pull per page
     * @return                   A list of the last count statuses for the user's list
     */
    public ResponseList<Status> getUserListStatuses(String ownerScreenName, String slug, int page, int count) {
    	if(inited) {
    		if(!UtilMethods.isSet(page)) page = 1;
        	if(!UtilMethods.isSet(count)) count = 20;
        	
            try {
                return twitter.getUserListStatuses(ownerScreenName, slug, new Paging(page, count));
            } catch (Exception e) {
                Logger.error(this, "Error Fetching list timeline", e);
                return null;
            }
        } else {
            Logger.warn(this, "ViewTool not inited");
            return null;
        }
    }
    
    /**
     * Gets a list of tweets from a user's list 
     * 
     * See {@link twitter4j.Twitter.getUserListStatuses}
     * 
     * @param ownerId    The list owner's id
     * @param slug       The list's slug
     * @param page       The page to pull
     * @param count      The number of items to pull per page
     * @return           A list of the last count statuses for the user's list
     */
    public ResponseList<Status> getUserListStatuses(long ownerId, String slug, int page, int count) {
    	if(inited) {
    		if(!UtilMethods.isSet(page)) page = 1;
        	if(!UtilMethods.isSet(count)) count = 20;
        	
            try {
                return twitter.getUserListStatuses(ownerId, slug, new Paging(page, count));
            } catch (Exception e) {
                Logger.error(this, "Error Fetching list timeline", e);
                return null;
            }
        } else {
            Logger.warn(this, "ViewTool not inited");
            return null;
        }
    }

}
