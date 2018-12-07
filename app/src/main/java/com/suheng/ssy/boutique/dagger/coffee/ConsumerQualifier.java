package com.suheng.ssy.boutique.dagger.coffee;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by wbj on 2018/12/7.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ConsumerQualifier {
}
