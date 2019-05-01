export default class ClientLibrary {

    constructor(someBasePath, someConfig) {
        this.basePath = someBasePath
        this.config = Object.assign({ mode: 'cors' }, someConfig)
    }

    url(aUrl) {
        let base = this.basePath

        return base? base + "/" + aUrl : aUrl
    }

    signup(aUsername, aPassword) {
        let body = {username: aUsername, password: aPassword}

        return fetch(url("signup"), Object.assign({
            method: "POST",
            body: JSON.stringifiy(body)
        }, this.config))
    }

    login(aUsername, aPassword) {
        let self = this;
        let body = {username: aUsername, password: aPassword}

        return fetch(url("signup"), Object.assign({
            method: "POST",
            body: JSON.stringifiy(body)
        }, this.config)).then(success => {
            self.username = aUsername
            self.password = aPassword
        })
    }

    create(game) {
        let isGameValid = isInt(game.width) && isInt(game.height) && isInt(game.mines)

        if(!isGameValid) throw new Error("Game must contain width, height and mines number")

        let headers = new Headers();
        headers.set('Authorization', 'Basic ' + atob(username + ":" + password));

        return fetch("games", Object.assign({
             method: "POST",
             body: JSON.stringifiy(game),
             headers: headers
         }, this.config))
    }

    getGames(gameId) {
        let gamesUrl = isInt(gameId) ? url("games/" + gameId) : url("games")

        let headers = new Headers();
        headers.set('Authorization', 'Basic ' + atob(username + ":" + password));


        return fetch(gamesUrl, Object.assign(
             headers: headers
         }, this.config))
    }

    mark(gameId, spot, move) {
        let isSpotValid = isInt(spot.x) && isInt(spot.y)
        let isValidMove = move == "mark" || move == ""

        if(!isSpotValid || !isValidMove)
            throw new Error("Spot should have coordinates x and y and be integers. Move is either 'mark' or 'sweep'")

        let headers = new Headers();
        headers.set('Authorization', 'Basic ' + atob(username + ":" + password));

        return fetch(url("games/" + gameId + "/mark"), Object.assign({
             method: "PUT",
             body: JSON.stringifiy(spot),
             headers: headers
         }, this.config))
    }

    sweep(gameId) {}
}

function isInt(value) {
    return Number.isInteger(Number(value))
}