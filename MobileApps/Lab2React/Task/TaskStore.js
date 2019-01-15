import { httpApiUrl, wsApiUrl } from '../core/api';
import React, {Component} from 'react';
import {Provider} from './context';
import {getLogger} from "../core/utils";

const log = getLogger('TaskStore');

class TaskStore extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isLoading: false,
      tasks: null,
      issue: null,
    };
  }

  componentDidMount() {
    log('componentDidMount');
    this.loadTasks();
    this.connectWs();
  }

  componentWillUnmount() {
    log('componentWillUnmount');
    this.disconnectWs();
  }

  loadTasks = () => {
    this.setState({ isLoading: false, issue: null });
    fetch(`${httpApiUrl}/tasks/0`)
      .then(response => response.json())
      .then(json => this.setState({ isLoading: false, tasks: json.tasks }))
      .catch(error => this.setState({ isLoading: false, issue: error }));
  };

  connectWs = () => {
    const ws = new WebSocket(wsApiUrl);
    ws.onopen = () => {};
    ws.onmessage = e => this.setState({
      tasks: [JSON.parse(e.data).tasks].concat(this.state.tasks || [])
    });
    ws.onerror = e => {};
    ws.onclose = e => {};
    this.ws = ws;
  };

  disconnectWs = () => {
    this.ws.close();
  };

  render() {
    return (
      <Provider value={this.state}>
        {this.props.children}
      </Provider>
    );
  }
}

export default TaskStore;