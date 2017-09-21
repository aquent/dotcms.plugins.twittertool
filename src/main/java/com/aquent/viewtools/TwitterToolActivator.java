package com.aquent.viewtools;

import com.dotmarketing.osgi.GenericBundleActivator;

import org.osgi.framework.BundleContext;

/**
 * Activator Class for the TwitterTool.
 * @author cfalzone
 */
public class TwitterToolActivator extends GenericBundleActivator {

    @Override
    public void start(BundleContext bundleContext) throws Exception {

        //Initializing services...
        initializeServices(bundleContext);

        //Registering the ViewTool service
        registerViewToolService(bundleContext, new TwitterToolInfo());
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        unregisterViewToolServices();
    }

}
