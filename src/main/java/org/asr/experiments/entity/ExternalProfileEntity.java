package org.asr.experiments.entity;

import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@SuperBuilder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ExternalProfileEntity {
    String linkedinProfileUrl;
    String githubProfileUrl;
    String twitterProfileUrl;
    String facebookProfileUrl;
    String instagramProfileUrl;
    String stackoverflowProfileUrl;
    String mediumProfileUrl;
    String youtubeProfileUrl;
    String websiteUrl;
    String blogUrl;
    Set<String> otherProfileUrlSet;

    public boolean isEmpty() {
        return linkedinProfileUrl == null && githubProfileUrl == null && twitterProfileUrl == null && facebookProfileUrl == null && instagramProfileUrl == null && stackoverflowProfileUrl == null && mediumProfileUrl == null && youtubeProfileUrl == null && websiteUrl == null && blogUrl == null && (otherProfileUrlSet == null || otherProfileUrlSet.isEmpty());
    }
}
