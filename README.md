Twitter Viewtool
=================================================
A plugin for dotCMS that adds a viewtool for interacting with Twitter

Installation
==================================
* Download and extract the plugin package to your {DOTCMS_ROOT}/plugins directory
* You will need to get twitter auth tokens from twitter
* Configure Twitter4J

Twitter4J Configuration
===================================
You can configure Twitter4J either by placing the properties in your startup script in the JAVA_OPTS or by setting environment variables:

To pass the variables in your startup script add the following to your JAVA_OPTS:
  -Dtwitter4j.oauth.consumerKey=you get this from twitter
  -Dtwitter4j.oauth.consumerSecret=you get this from twitter
  -Dtwitter4j.oauth.accessToken=you get this from twitter
  -Dtwitter4j.oauth.accessTokenSecret=you get this from twitter
  
For More information see their website:  http://twitter4j.org/en/configuration.html

Usage:
======
TODO:  Needs some better doc here but this is the idea anyway:

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

## Get a user's timeline
#set($statuses = $twitter.getUserTimeline("chris_falzone", 1, 5))
<h2> Fetched $statuses.size() statuses </h2>
<dl>
#foreach($status in $statuses)
  #displayStatus($status)
#end
</dl>

<br style="clear:both" /><hr />

## Get a User's followers
#set($followers = $twitter.getFollowersList("chris_falzone"))
<h2> Found $followers.size() Followers:</h2>
<dl>
#foreach($follower in $followers)
  <dt> ${follower.getScreenName()}'s Info</dt>
  <dd> #displayUser($follower) </dd>
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