import {Component, inject, OnInit} from '@angular/core';
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import {NgClass} from "@angular/common";
import {TopicService} from "../../core/services/topic.service";
import {Topic} from "../../core/models/topic";
import {SubscriptionService} from "../../core/services/subscription.service";
import {TopicSubscription} from "../../core/models/topic-subscription";

@Component({
  selector: 'app-topics',
  imports: [
    NavbarComponent,
    NgClass
  ],
  standalone: true,
  templateUrl: './topics.component.html',
  styleUrl: './topics.component.scss'
})
export class TopicsComponent implements OnInit {
  private topicService: TopicService = inject(TopicService);
  private subscriptionService: SubscriptionService = inject(SubscriptionService);

  topics: Topic[] = [];
  errorMessage: string | null = null;
  subscribedTopicIds: number[] = [];

  ngOnInit(): void {
    this.topicService.getAllTopics().subscribe({
      next: (data: Topic[]): Topic[] => this.topics = data,
      error: () => this.errorMessage = "Erreur lors du chargement des thèmes"
    });

    this.subscriptionService.getUserSubscriptions().subscribe({
      next: (subscriptions: TopicSubscription[]): number[] => this.subscribedTopicIds = subscriptions.map((subscription: TopicSubscription): number => subscription.topicId)
    });
  }

  isSubscribed(topicId: number): boolean {
    return this.subscribedTopicIds.includes(topicId);
  }

  toggleSubscription(topicId: number): void {
    if (this.isSubscribed(topicId)) {
      this.subscriptionService.unsubscribe(topicId).subscribe({
        next: (): void => {
          this.subscribedTopicIds = this.subscribedTopicIds.filter(id => id !== topicId);
        }
      });
    } else {
      this.subscriptionService.subscribe(topicId).subscribe({
        next: (): void => {
          this.subscribedTopicIds.push(topicId);
        }
      });
    }
  }

}
