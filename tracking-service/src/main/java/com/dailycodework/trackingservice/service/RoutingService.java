package com.dailycodework.trackingservice.service;



public interface RoutingService {

   RouteSummary calculateRoute(
            Double startLatitude,
            Double startLongitude,
            Double destinationLatitude,
            Double destinationLongitude
    );
}