## Coding Task  ##

Implementer - John Regan (explicitly.io)

Email - johnregancontracting@gmail.com

Design

ThirdpartyAPI added as a trait that can be passed to other services

RestAPI defined as trait, but not implemented. Would only need to call SocialMetricService

SocialMetricService handles the api specific requests with the required business logic

Connection Counter provides the ability to gather first/second degree connection information for a given person

Test suite can be run with `sbt test`

