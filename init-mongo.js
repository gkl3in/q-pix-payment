db.createUser(
    {
        user: "root",
        pwd: "gklein",
        roles: [
            {
                role: "readWrite",
                db: "pix"
            }
        ]
    }
);
db.createCollection("transacao_pix");