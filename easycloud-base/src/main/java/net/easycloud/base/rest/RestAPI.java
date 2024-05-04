package net.easycloud.base.rest;

import net.easycloud.base.Base;

import static spark.Spark.get;

public final class RestAPI {
    public RestAPI() {
        get("/valid", (req, res) -> checkIfValid(req.queryParams("adminKey")));
        get("/users/size", (req, res) -> {
            if(!checkIfValid(req.queryParams("adminKey"))) return "ERROR";
            return Base.getInstance().getUserProvider().getUsers().size();
        });
        get("/groups/size", (req, res) -> {
            if(!checkIfValid(req.queryParams("adminKey"))) return "ERROR";
            return Base.getInstance().getGroupProvider().getRepository().query().database().findAll().size();
        });
    }

    private boolean checkIfValid(String key) {
        return Base.getInstance().getConfiguration().adminKey().equals(key);
    }
}
