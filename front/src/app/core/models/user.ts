import {Topic} from './topic';

export interface User {
  id: number;
  email: string;
  username: string;
  subscribedTopics: Topic[];
}
