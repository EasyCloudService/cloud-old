package net.easycloud.base.rest;

import net.easycloud.base.Base;

import static spark.Spark.get;

public final class RestAPI {
    public RestAPI() {
        get("/valid", (req, res) -> {
            return checkIfValid(req.params(":key"));
        });
        get("/users/size", (req, res) -> {
            if(!checkIfValid(req.params(":key"))) return "ERROR";
            return Base.getInstance().getPermissionProvider().getUsers().size();
        });
        get("/groups/size", (req, res) -> {
            if(!checkIfValid(req.params(":key"))) return "ERROR";
            return Base.getInstance().getGroupProvider().getRepository().query().database().findAll().size();
        });
    }

    private boolean checkIfValid(String key) {
        return Base.getInstance().getConfiguration().adminKey().equals(key);
    }
}
