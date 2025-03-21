import { CommentResponse } from './comment-response';

export interface ArticleResponse {
  id: number;
  title: string;
  content: string;
  createdAt: Date;
  author: string;
  topic: string;
  comments: CommentResponse[];
}
