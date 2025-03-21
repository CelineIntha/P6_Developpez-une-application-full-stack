import {ArticleComment} from './article-comment';

export interface Article {
  id: number;
  title: string;
  content: string;
  createdAt: Date;
  author: string;
  topic: string;
  topicId: number;
  comments: ArticleComment[];
}
