package com.pg.s3snssqs;

import java.io.Closeable;
import java.io.Serializable;
import java.util.Collection;

import org.burningwave.core.assembler.ComponentContainer;
import org.burningwave.core.assembler.ComponentSupplier;
import org.burningwave.core.classes.ClassCriteria;
import org.burningwave.core.classes.ClassHunter;
import org.burningwave.core.classes.SearchConfig;
import software.amazon.awscdk.core.IInspectable;
import software.amazon.awscdk.services.s3.IBucketNotificationDestination;
import software.amazon.awscdk.services.s3.NotificationKeyFilter;
import software.amazon.awscdk.services.sns.CfnSubscription;

public class Finder {

    public Collection<Class<?>> find() {

        ComponentSupplier componentSupplier = ComponentContainer.getInstance();
        ClassHunter classHunter = componentSupplier.getClassHunter();

        //With this the search will be executed on default configured paths that are the
        //runtime class paths plus, on java 9 and later, the jmods folder of the Java home.
        //The default configured paths are indicated in the 'paths.hunters.default-search-config.paths'
        //property of burningwave.properties file (see Architectural overview and configuration)
        try (ClassHunter.SearchResult searchResult = classHunter.findBy(
                SearchConfig.byCriteria(
                        ClassCriteria.create().allThoseThatMatch(currentScannedClass ->
                                NotificationKeyFilter.class.isAssignableFrom(currentScannedClass)
                        )
                )
        )) {
            return searchResult.getClasses();
        }
    }

    public static void main(String[] args) {
        Finder finder =new Finder();
        Collection<Class<?>> classes = finder.find();
        System.out.println(" classes : "+classes.getClass().getSimpleName());
    }

}
