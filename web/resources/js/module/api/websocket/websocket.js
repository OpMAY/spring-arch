'use strict'

const initializeSocket = ({
                              url,
                              onOpen = (event, self) => {
                                  console.log('onOpen');
                              },
                              onMessage = (event, self) => {
                                  console.log('onMessage',);
                              },
                              onClose = (event, self) => {
                                  console.log('onClose');
                              },
                              onError = (event, self) => {
                                  console.log('onError');
                              },
                              onSend = (data, self) => {
                                  console.log(`onSend -> ${JSON.stringify(data)}`);
                                  self.send(JSON.stringify(data));
                              },
                              disconnect = () => {
                                  console.log('disconnect');
                                  this.close();
                              },
                          }) => {
    //Initialize
    let WEBSOCKET = new WebSocket(url);
    //Method
    WEBSOCKET.addEventListener('open', function (event) {
        onOpen(event, this);
    });
    WEBSOCKET.addEventListener('message', function (event) {
        onMessage(event, this);
    });
    WEBSOCKET.addEventListener('close', function (event) {
        onClose(event, this);
    });
    WEBSOCKET.addEventListener('error', function (event) {
        onError(event, this);
    });
    WEBSOCKET.onSend = onSend;
    WEBSOCKET.disconnect = disconnect;
    return WEBSOCKET;
}