Instalacia operatorov pre infinispan a kafka
--------------------------------------------
```./install-operators.sh```

Nasadenie infinispan + kafka + data-index
-----------------------------------------
```./deploy-kogito-infra.sh```

Dalej je potrebne upravit URL adresy pre kafku a data-index v kogito-runtime-process.yaml,
kogito-management-console.yaml a kogito-task-console.yaml podla IP adries, ktore zistime
prikazom kubectl get services.

Nasadenie kogito runtime service + management + task console
------------------------------------------------------------
```./deploy-runtime-and-consoles.sh```


Pridanie usera do task console
------------------------------
- otvorit task console v browsri
- kliknut vpravo hore na sipku, vybrat z menu Add new user, pridat usera userId: officer groups: officer


Spustenie procesu
-----------------
```POST http://<service_approval_process_ip_address>/approvals?businessKey=789```

Request body:

```
{
    "isApproved": false,
    "isAssigned": false,
    "isPublished": false,
    "serviceData": {
        "serviceId": "789",
        "status": "STARTED"
    }
}
```

Posunutie procesu do dalsej fazy
--------------------------------
- otvorit v browsri graphql konzolu data indexu
- spustit query, ktorou zistime id user tasku (task_id):
```
{
  UserTaskInstances {
    id
  }
}
```

```GET http://<service_approval_process_ip_address>/approvals - ziskanie id instancii procesu (approval_id)```

```POST http://<service_approval_process_ip_address>/approvals/<approval_id>/serviceSelectionAndAssessment/<task_id>/phases/complete?group=officer&user=officer```

Request body:
```
{
  "isFromUserAssigned": true,
  "service": {
    "serviceId": "789",
    "status": "ASSIGNED"
  }
}
```