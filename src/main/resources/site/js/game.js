import client from '/site/js/client.js'

const SWEEP = "sweep"
const MARK = "mark"

$("#grid").delegate(".cell", "mouseup", (e) => {
    let gameId = Number($(e.delegateTarget).attr("data-game-id"))
    let spot = JSON.parse(unescape($(e.currentTarget).attr("data-pos")))
    let markOrSweep = e.which == 3 ? MARK : SWEEP

    client
        .move(gameId, markOrSweep, spot)
        .then(fillGrid)
})

$("#newgame").click(e => {
    let game = readGameParams()

    client
        .create(game)
        .then(fillGrid)
        .then(loadGames)
})

$("#gameslist").delegate(".mybutton.gameitem", "click", e => {
    let gameId = $(e.target).attr("data-id")

    client.getGames(gameId).then(fillGrid)
})

function readGameParams() {
    let width = Number($("#width").val())
    let height = Number($("#height").val())
    let mines = Number($("#mines").val())

    return { width, height, mines }
}

function buildGameElem(gameElem) {
    switch(gameElem) {
        case 'Hidden':      return ''
        case 'Empty':       return '<div class="empty"></div>'
        case 'Mine':        return '<i class="fas fa-bomb"></i>'
        case 'MarkedMine':  return '<i class="fas fa-flag"></i>'
        default:            return '<span class="number">{{number}}</span>'.replace("{{number}}", gameElem)
    }
}

function fillGrid(game) {
    let actualGrid = game.grid.grid

    let rows = actualGrid.map((row, y) => {
        let rowTemplate = '<div class="row">{{cells}}</div>'
        let cellTemplate = '<div class="cell" data-pos="{{spot}}">{{gameElement}}</div>'

        let cells = row.map(buildGameElem).map((built, x) => {
            return cellTemplate
                .replace("{{spot}}", escape(JSON.stringify({x, y})))
                .replace("{{gameElement}}", built)
        }).join("")

        return rowTemplate.replace("{{cells}}", cells)
    })

    $("#grid")
        .attr("data-game-id", game.id)
        .html(rows.join(""))
        .css("visibility", "visible")

    return game;
}

export function loadGames() {
    return client
        .getGames()
        .then(games => {
            return games.map(game => {
                let grid = game.grid
                let template =
                    `<button data-id="{{gameId}}" class="mybutton gameitem">
                        width: {{width}} height: {{height}} mines: {{mines}} status: {{status}}
                    </button>`

                return template
                    .replace("{{gameId}}", game.id)
                    .replace("{{width}}", grid.width)
                    .replace("{{height}}", grid.height)
                    .replace("{{mines}}", grid.mines)
                    .replace("{{status}}", grid.status)
            })
        }).then(elements => {
            if(elements.length > 0)
                $("#gameslist").html(elements.join("")).show()
        })
}