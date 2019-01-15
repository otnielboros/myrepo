import React from 'react';
import TaskStore from './TaskStore.js';
import {TaskList} from './TaskList.js';

export default () => (
  <TaskStore>
    <TaskList/>
  </TaskStore>
);