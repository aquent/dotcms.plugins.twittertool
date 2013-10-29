Twitter OSGI Viewtool
=================================================
An osgi plugin for dotCMS that adds a viewtool for interacting with Twitter using Twitter4J.

Installation
==================================
* Take the .jar from build/jar and upload it to 

Twitter4J Configuration
===================================
You need to create the following host variables and enter the values in your Default Host:

twitter4JConsumerKey - Text
twitter4jConsumerSecret - Text
twitter4jAccessToken - Text
twitter4jAccessTokenSecret - Text
twitter4jDebug - Boolean (Select or Radio with True/False)
  
For More information see the Twitter4J website:  http://twitter4j.org/en/configuration.html

Usage:
======
You will want to have to Twitter4J JavaDoc handy:  http://twitter4j.org/javadoc/index.html

The tool is mapped to the key $twitter and currently has the following methods implemented:
* showUser - takes a twitter screen name or user id and returns a Twitter4J User Object:  http://twitter4j.org/javadoc/twitter4j/User.html
* getUserTimeline - takes a twitter screen name or userid, a page number, and a count and returns a List of Twitter4J Status Objects:  http://twitter4j.org/javadoc/twitter4j/Status.html
* getFollowersList - takes a twitter screen name or userid and returns the first 20 followers for that user.  The list returned contains Twitter4J User Objects.  Note that this method is not working so well currently due to the twitter apoi rate limits on fetching this data.  If you plan on using this I would seriously reccomend using a block cache around the method
* getUserListMembers - takes a twitter screen name or userid, and a list slug and returns up to 20 members of the list.  The list returned contains Twitter4J Objects.  Same note about twitter api rate limit applies.
* getUserListStatuses - takes a twitter screen name or userid, a list slug, a page number, and a count and returns a list of Twitter4J Status Objects for the user's list.

If you would like to see more methods implemented please request them by submitting an issue to this github.

If a method is not working check your log file, you probably exceeded the twitter api rate limit.  I would suggest wrapping any code that uses this in a block cache directive:  http://dotcms.com/docs/latest/CacheHandlingWithTags

```velocity
## Just defining a macro to display a status
#macro(displayStatus $status)
  <dt> Status ID: $status.getId() </dt>
  <dd>
    <dl>
      
      ## Get the contributor IDs
      <dt>Contributor IDs:</dt>
      <dd><ul>
        #foreach($contributor in $status.getContributors())
          <li>$contributor</li>
        #end
      </ul></dd>
      
      ## Status Created Date
      <dt>Created At:</dt>
      <dd>$status.getCreatedAt()</dd>
      
      ## Get the GeoLocation of the tweet
      #set($geo = $status.getGeoLocation())
      <dt>GeoLocation:</dt>
      #if($UtilMethods.isSet($geo))
        <dd>Lat: $geo.getLatitude(), Long: $geo.getLongitude()</dd>
      #else
        <dd>Not Available</dd>
      #end
      
      ## Get The Place
      #set($place = $status.getPlace())
      <dt>Place:</dt>
      #if($UtilMethods.isSet($place))
        <dd><dl>
          <dt>ID</dt>
          <dd>$place.getId()</dd>
          <dt>Name</dt>
          <dd>$place.getName()</dd>
          <dt>URL</dt>
          <dd>$place.getURL()</dd>
          <dt>More Info about place:</dt>
          <dd><a href="http://twitter4j.org/oldjavadocs/3.0.3/twitter4j/Place.html">JavaDoc</a></dd>
        </dl></dd>
      #else
        <dd>Not Available</dd>
      #end
      
      ## The status source:
      <dt>Source:</dt>
      <dd>$status.getSource()</dd>
      
      ## The Status Text:
      <dt>The Tweet:</dt>
      <dd>$status.getText()</dd>
    </dl>
  </li>
#end

## Define a macro to show a user
#macro(displayUser $tUser)
<dl>
  <dt>User ID</dt>
  <dd>$tUser.getId()</dd>
  <dt>User Name</dt>
  <dd>$tUser.getName()</dd>
  <dt>Profile Image</dt>
  <dd><img src="${tUser.getProfileImageURL()}" alt="${tUser.getName()}'s Profile Image"/></dd>
</dl>
#end

## Get user's Info
#set($tUser = $twitter.showUser("chris_falzone"))
<h2> chris_falzone's Info </h2>
#displayUser($tUser)

<br style="clear:both" /><hr />

#set($tUser2 = $twitter.showUser(15378515))
<h2>  User By ID </h2>
#displayUser($tUser2)

<br style="clear:both" /><hr />

## Get a user's timeline
#set($statuses = $twitter.getUserTimeline("chris_falzone", 1, 5))
<h2> Fetched $statuses.size() statuses </h2>
<dl>
#foreach($status in $statuses)
  #displayStatus($status)
#end
</dl>

<br style="clear:both" /><hr />

## Get a User's followers - See note above about twitter api rate limit 
#set($followers = $twitter.getFollowersList("chris_falzone"))
<h2> Found $followers.size() Followers:</h2>
<dl>
#foreach($follower in $followers)
  <dt> ${follower.getScreenName()}'s Info</dt>
  <dd> #displayUser($follower) </dd>
#end
</dl>

## Get a User List's Members
#set($members = $twitter.getUserListMembers('suzillazilla', 'team'))
<h2> Members:</h2>
<dl>
#foreach($member in $members)
  <dt> ${member.getScreenName()}'s Info</dt>
  <dd> #displayUser($member) </dd>
#end
</dl>

<br style="clear:both" /><hr />

## Get a user's list:
#set($statuses = $twitter.getUserListStatuses('suzillazilla', 'team', 1, 20))
<h2> Fetched $statuses.size() statuses </h2>
<dl>
#foreach($status in $statuses)
  #displayStatus($status)
#end
</dl>
```

Additional Info:
====================================
This plugin includes software from Twitter4J.org.  You can see the license term at http://twitter4j.org/en/index.html#license
