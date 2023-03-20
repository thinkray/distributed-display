# Distributed Display

Distributed Display allows centralized display content management of multiple client nodes.

<img src="./.github/server.png?raw=true" width="350">
<img src="./.github/client.png?raw=true" width="350">

## How to build the project

1. Install [Maven](https://maven.apache.org/install.html).
   
2. Clone the repository
   
    ```bash
    git clone https://github.com/thinkray/distributed-display.git
    ```

3. Build the `core` module and install it in your local repository.

    ```bash
    cd core
    mvn package
    mvn install
    ```

4. Build the `server` and `client`.

    ```bash
    cd ..
    cd server
    mvn assembly:single

    cd ..
    cd client
    mvn assembly:single
    ```

5. The `.jar` executable is located in `serer/target` and `client/target`.

## License
This project, including the documentation, is licensed under the MIT license. Copyright (c) 2022