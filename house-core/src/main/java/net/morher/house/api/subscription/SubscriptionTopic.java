package net.morher.house.api.subscription;

public interface SubscriptionTopic<L> {

    Subscription subscribe(L item);
    
}