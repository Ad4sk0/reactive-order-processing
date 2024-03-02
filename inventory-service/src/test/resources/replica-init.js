rs.initiate({
    _id: "mainReplicaSet",
    members: [
        { _id: 0, host: "mongo:27017" }
    ]
})
