workspace {
    model {
        user = person "User"

        softwareSystem = softwareSystem "System" {
            service = container "Service" "A Demo application that services REST endpoint to greet the user"{
                controller = component "RootController" "Serves Hello World greeting for users"
            }
            postgres = container "PostgreSQL" "A Dummy database to hold possible information" "Database"
        }

        user -> softwareSystem "Uses"
    }

    views {
        systemContext softwareSystem {
            include *
            autolayout lr
        }
        container softwareSystem {
            include *
            autoLayout lr
        }
        component service {
            include *
            autoLayout
        }

        styles {
            element "Person" {
                color #ffffff
                fontSize 22
                shape Person
                background #08427b
            }
            element "Software System" {
                background #1168bd
                color #ffffff
            }
            element "Existing System" {
                background #999999
                color #ffffff
            }
            element "Container" {
                background #438dd5
                color #ffffff
            }
            element "Web Browser" {
                shape WebBrowser
            }
            element "Database" {
                shape Cylinder
            }
            element "Component" {
                background #85bbf0
                color #000000
            }
            element "Failover" {
                opacity 25
            }
        }
    }
}
