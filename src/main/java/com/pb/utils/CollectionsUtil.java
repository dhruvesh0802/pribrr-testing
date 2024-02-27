package com.pb.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionsUtil {
    
    public static boolean checkCollectionEmptyAndNull(Collection<?> collection){
        return  (collection != null && !collection.isEmpty());
    }
}
