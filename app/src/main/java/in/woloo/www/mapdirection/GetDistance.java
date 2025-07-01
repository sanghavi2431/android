package in.woloo.www.mapdirection;

import java.util.List;


@SuppressWarnings("all")
public class GetDistance {
    private final List<GeocodedWaypoints> geocodedWaypoints;

    private final List<Routes> routes;

    private final String status;

    public GetDistance(List<GeocodedWaypoints> geocodedWaypoints, List<Routes> routes,
                       String status) {
        this.geocodedWaypoints = geocodedWaypoints;
        this.routes = routes;
        this.status = status;
    }

    public List<GeocodedWaypoints> getGeocodedWaypoints() {
        return geocodedWaypoints;
    }

    public List<Routes> getRoutes() {
        return routes;
    }

    public String getStatus() {
        return status;
    }

    public static class GeocodedWaypoints {
        private final String geocoderStatus;

        private final String placeId;

        private final List<String> types;

        public GeocodedWaypoints(String geocoderStatus, String placeId, List<String> types) {
            this.geocoderStatus = geocoderStatus;
            this.placeId = placeId;
            this.types = types;
        }

        public String getGeocoderStatus() {
            return geocoderStatus;
        }

        public String getPlaceId() {
            return placeId;
        }

        public List<String> getTypes() {
            return types;
        }
    }

    public static class Routes {
        private final Bounds bounds;

        private final String copyrights;

        private final List<Legs> legs;

        private final OverviewPolyline overview_polyline;

        private final String summary;

        private final List<Object> warnings;

        private final List<Object> waypointOrder;

        public Routes(Bounds bounds, String copyrights, List<Legs> legs,
                      OverviewPolyline overview_polyline, String summary, List<Object> warnings,
                      List<Object> waypointOrder) {
            this.bounds = bounds;
            this.copyrights = copyrights;
            this.legs = legs;
            this.overview_polyline = overview_polyline;
            this.summary = summary;
            this.warnings = warnings;
            this.waypointOrder = waypointOrder;
        }

        public Bounds getBounds() {
            return bounds;
        }

        public String getCopyrights() {
            return copyrights;
        }

        public List<Legs> getLegs() {
            return legs;
        }

        public OverviewPolyline getOverview_polyline() {
            return overview_polyline;
        }

        public String getSummary() {
            return summary;
        }

        public List<Object> getWarnings() {
            return warnings;
        }

        public List<Object> getWaypointOrder() {
            return waypointOrder;
        }

        public static class Bounds {
            private final Northeast northeast;

            private final Southwest southwest;

            public Bounds(Northeast northeast, Southwest southwest) {
                this.northeast = northeast;
                this.southwest = southwest;
            }

            public Northeast getNortheast() {
                return northeast;
            }

            public Southwest getSouthwest() {
                return southwest;
            }

            public static class Northeast {
                private final double lat;

                private final double lng;

                public Northeast(double lat, double lng) {
                    this.lat = lat;
                    this.lng = lng;
                }

                public double getLat() {
                    return lat;
                }

                public double getLng() {
                    return lng;
                }
            }

            public static class Southwest {
                private final double lat;

                private final double lng;

                public Southwest(double lat, double lng) {
                    this.lat = lat;
                    this.lng = lng;
                }

                public double getLat() {
                    return lat;
                }

                public double getLng() {
                    return lng;
                }
            }
        }

        public static class Legs {
            private final Distance distance;

            private final Duration duration;

            private final String endAddress;

            private final EndLocation endLocation;

            private final String startAddress;

            private final StartLocation startLocation;

            private final List<Steps> steps;

            private final List<Object> trafficSpeedEntry;

            private final List<Object> viaWaypoint;

            public Legs(Distance distance, Duration duration, String endAddress,
                        EndLocation endLocation, String startAddress, StartLocation startLocation,
                        List<Steps> steps, List<Object> trafficSpeedEntry, List<Object> viaWaypoint) {
                this.distance = distance;
                this.duration = duration;
                this.endAddress = endAddress;
                this.endLocation = endLocation;
                this.startAddress = startAddress;
                this.startLocation = startLocation;
                this.steps = steps;
                this.trafficSpeedEntry = trafficSpeedEntry;
                this.viaWaypoint = viaWaypoint;
            }

            public Distance getDistance() {
                return distance;
            }

            public Duration getDuration() {
                return duration;
            }

            public String getEndAddress() {
                return endAddress;
            }

            public EndLocation getEndLocation() {
                return endLocation;
            }

            public String getStartAddress() {
                return startAddress;
            }

            public StartLocation getStartLocation() {
                return startLocation;
            }

            public List<Steps> getSteps() {
                return steps;
            }

            public List<Object> getTrafficSpeedEntry() {
                return trafficSpeedEntry;
            }

            public List<Object> getViaWaypoint() {
                return viaWaypoint;
            }

            public static class Distance {
                private final String text;

                private final int value;

                public Distance(String text, int value) {
                    this.text = text;
                    this.value = value;
                }

                public String getText() {
                    return text;
                }

                public int getValue() {
                    return value;
                }
            }

            public static class Duration {
                private final String text;

                private final int value;

                public Duration(String text, int value) {
                    this.text = text;
                    this.value = value;
                }

                public String getText() {
                    return text;
                }

                public int getValue() {
                    return value;
                }
            }

            public static class EndLocation {
                private final double lat;

                private final double lng;

                public EndLocation(double lat, double lng) {
                    this.lat = lat;
                    this.lng = lng;
                }

                public double getLat() {
                    return lat;
                }

                public double getLng() {
                    return lng;
                }
            }

            public static class StartLocation {
                private final double lat;

                private final double lng;

                public StartLocation(double lat, double lng) {
                    this.lat = lat;
                    this.lng = lng;
                }

                public double getLat() {
                    return lat;
                }

                public double getLng() {
                    return lng;
                }
            }

            public static class Steps {
                private final Distance distance;

                private final Duration duration;

                private final EndLocation endLocation;

                private final String htmlInstructions;



                private final String maneuver;

                private final Polyline polyline;

                private final StartLocation startLocation;

                private final String travelMode;

                public Steps(Distance distance, Duration duration, EndLocation endLocation,
                             String htmlInstructions, Polyline polyline, StartLocation startLocation,
                             String travelMode,String maneuver) {
                    this.distance = distance;
                    this.duration = duration;
                    this.endLocation = endLocation;
                    this.htmlInstructions = htmlInstructions;
                    this.polyline = polyline;
                    this.startLocation = startLocation;
                    this.travelMode = travelMode;
                    this.maneuver=maneuver;
                }

                public Distance getDistance() {
                    return distance;
                }

                public Duration getDuration() {
                    return duration;
                }

                public EndLocation getEndLocation() {
                    return endLocation;
                }

                public String getHtmlInstructions() {
                    return htmlInstructions;
                }
                public String getManeuver() {
                    return maneuver;
                }

                public Polyline getPolyline() {
                    return polyline;
                }

                public StartLocation getStartLocation() {
                    return startLocation;
                }

                public String getTravelMode() {
                    return travelMode;
                }

                public static class Distance {
                    private final String text;

                    private final int value;

                    public Distance(String text, int value) {
                        this.text = text;
                        this.value = value;
                    }

                    public String getText() {
                        return text;
                    }

                    public int getValue() {
                        return value;
                    }
                }

                public static class Duration {
                    private final String text;

                    private final int value;

                    public Duration(String text, int value) {
                        this.text = text;
                        this.value = value;
                    }

                    public String getText() {
                        return text;
                    }

                    public int getValue() {
                        return value;
                    }
                }

                public static class EndLocation {
                    private final double lat;

                    private final double lng;

                    public EndLocation(double lat, double lng) {
                        this.lat = lat;
                        this.lng = lng;
                    }

                    public double getLat() {
                        return lat;
                    }

                    public double getLng() {
                        return lng;
                    }
                }

                public static class Polyline {
                    private final String points;

                    public Polyline(String points) {
                        this.points = points;
                    }

                    public String getPoints() {
                        return points;
                    }
                }

                public static class StartLocation {
                    private final double lat;

                    private final double lng;

                    public StartLocation(double lat, double lng) {
                        this.lat = lat;
                        this.lng = lng;
                    }

                    public double getLat() {
                        return lat;
                    }

                    public double getLng() {
                        return lng;
                    }
                }
            }
        }

        public static class OverviewPolyline {
            private final String points;

            public OverviewPolyline(String points) {
                this.points = points;
            }

            public String getPoints() {
                return points;
            }
        }
    }
}
