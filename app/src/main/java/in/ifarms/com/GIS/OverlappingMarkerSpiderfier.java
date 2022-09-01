package in.ifarms.com.GIS;

import android.graphics.Color;
import android.graphics.Point;

import com.androidmapsextensions.*;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class OverlappingMarkerSpiderfier {

    private static final String VERSION = "0.3.3"; //version of original code.
    private static final String LOGTAG = "MarkerSpiderfier";
    private static final double TWO_PI = Math.PI * 2;
    private static final double RADIUS_SCALE_FACTOR = 10; // TODO: needs to be computed according to the device px density & zoom lvl
    public static final double SPIRAL_ANGLE_STEP = 0.2; //in radians

    // Passable params' names
    private static final String ARG_KEEP_SPIDERFIED = "keepSpiderfied";
    private static final String ARG_MARK_WONT_HIDE = "markersWontHide";
    private static final String ARG_MARK_WONT_MOVE = "markersWontMove";
    private static final String ARG_NEARBY_DISTANCE = "nearbyDistance";
    private static final String ARG_CS_SWITCHOVER = "circleSpiralSwitchover";
    private static final String ARG_LEG_WEIGHT = "legWeight";

    private GoogleMap gm;
    private int mt; // map type
    private Projection proj;
    private boolean keepSpiderfied = false;

    private boolean markersWontHide = false;
    private boolean markersWontMove = false;

    private int nearbyDistance = 20;
    private int circleSpiralSwitchover = 9;
    private float legWeight = 3F;


    private int circleFootSeparation = 23; // related to circumference of circles
    private double circleStartAngle = TWO_PI / 12;
    private int spiralFootSeparation = 26; // related to size of spiral (experiment!)
    private int spiralLengthStart = 11;    // ditto
    private int spiralLengthFactor = 4;    // ditto

    private int spiderfiedZIndex = 1000;   // ensure spiderfied markersInCluster are on top
    private int usualLegZIndex = 0;       // for legs
    private int highlightedLegZIndex = 20; // ensure highlighted leg is always on top

    private class _omsData {
        private LatLng usualPosition;
        private Polyline leg;

        public LatLng getUsualPosition() {
            return usualPosition;
        }

        public Polyline getLeg() {
            return leg;
        }

        public _omsData leg(Polyline newLeg) {
            if (leg != null)
                leg.remove();
            leg = newLeg;
            return this; // return self, for chaining
        }

        public _omsData usualPosition(LatLng newUsualPos) {
            usualPosition = newUsualPos;
            return this; // return self, for chaining
        }

    }

    private class MarkerData {
        public Marker marker;
        public Point markerPt;
        public boolean willSpiderfy = false;

        public MarkerData(Marker mark, Point pt) {
            marker = mark;
            markerPt = pt;
        }

        public MarkerData(Marker mark, Point pt, boolean spiderfication) {
            marker = mark;
            markerPt = pt;
            willSpiderfy = spiderfication;
        }
    }

    private class LegColor {

        private final int type_satellite;
        private final int type_normal; // in the javascript version this is known as "roadmap"

        public LegColor(int set, int road) {
            type_satellite = set;
            type_normal = road;
        }

        public LegColor(String set, String road) {
            type_satellite = Color.parseColor(set);
            type_normal = Color.parseColor(road);
        }

        public int getType_satellite() {
            return type_satellite;
        }

        public int getType_normal() {
            return type_normal;
        }
    }

    public final LegColor usual = new LegColor(0xAAFFFFFF, 0xAA0F0F0F);
    public final LegColor highlighted = new LegColor(0xAAFF0000, 0xAAFF0000);

    private List<Marker> markersInCluster; // refers to the current clicked cluster
    private List<Marker> displayedMarkers;
    private List<Marker> spiderfiedClusters; // as the name suggests
    private List<Marker> spiderfiedUnclusteredMarkers; // intended to hold makers that were tightly packed but not clustered before spiderfying

    private boolean spiderfying = false;   //needed for recursive spiderfication
    private boolean unspiderfying = false; // TODO: check if needed for recursive unspiderfication
    private boolean isAnythingSpiderfied = false;
    private float zoomLevelOnLastSpiderfy;

    private HashMap<Marker, _omsData> omsData = new HashMap<Marker, _omsData>();
    private HashMap<Marker, Boolean> spiderfyable = new HashMap<Marker, Boolean>();

    public OverlappingMarkerSpiderfier(GoogleMap gm, Object... varArgs) throws IllegalArgumentException {
        this.gm = gm;
        mt = gm.getMapType();
        if (varArgs.length > 0)
            assignVarArgs(varArgs);
        initMarkerArrays();

        gm.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (spiderfiedClusters.size() > 0 && cameraPosition.zoom != zoomLevelOnLastSpiderfy)
                    unspiderfyAll();
            }
        });
    }

    private void initMarkerArrays() { //TODO: call on unspiderfyall?
        markersInCluster = new ArrayList<Marker>();
        displayedMarkers = new ArrayList<Marker>();
        spiderfiedClusters = new ArrayList<Marker>();
        spiderfiedUnclusteredMarkers = new ArrayList<Marker>();
    }

    private List<Point> generatePtsCircle(int count, Point centerPt) {
        int circumference = circleFootSeparation * (2 + count);
        double legLength = circumference / TWO_PI * RADIUS_SCALE_FACTOR; // = radius from circumference
        double angleStep = TWO_PI / count;
        double angle;
        List<Point> points = new ArrayList<Point>(count);
        for (int ind = 0; ind < count; ind++) {
            angle = circleStartAngle + ind * angleStep;
            points.add(new Point((int) (centerPt.x + legLength * Math.cos(angle)), (int) (centerPt.y + legLength * Math.sin(angle))));
        }
        return points;
    }

    /**
     * Corresponds to line 128 of original code
     */
    private List<Point> generatePtsSpiral(int count, Point centerPt) {
        double legLength = spiralLengthStart * RADIUS_SCALE_FACTOR;
        double angle = 0;
        List<Point> points = new ArrayList<Point>(count);
        for (int ind = 0; ind < count; ind++) {
            angle += spiralFootSeparation / legLength + ind * SPIRAL_ANGLE_STEP;
            points.add(new Point((int) (centerPt.x + legLength * Math.cos(angle)), (int) (centerPt.y + legLength * Math.sin(angle))));
            legLength += TWO_PI * spiralLengthFactor / angle;
        }
        return points;
    }

    public void spiderListener(Marker cluster) { /** Corresponds to line 138 of original code*/

        if (isAnythingSpiderfied && !spiderfying) { // unspiderfy everything before spiderfying anything new
            unspiderfyAll();
        }
        List<MarkerData> closeMarkers = new ArrayList<MarkerData>();
        List<MarkerData> displayedFarMarkers = new ArrayList<MarkerData>();
        int nDist = nearbyDistance;
        int pxSq = nDist * nDist;
        Point mPt, markerPt = llToPt(cluster.getPosition());
        List<Marker> tmpMarkersInCluster = new ArrayList<Marker>();
        tmpMarkersInCluster.addAll(cluster.getMarkers());
        markersInCluster.addAll(cluster.getMarkers());

        for (Marker markers_item : tmpMarkersInCluster) {
            if (markers_item.isCluster()) {
                recursiveAddMarkersToSpiderfy(markers_item);
            }
            mPt = proj.toScreenLocation(markers_item.getPosition());
            if (ptDistanceSq(mPt, markerPt) < pxSq)
                closeMarkers.add(new MarkerData(markers_item, mPt));
            else
                displayedFarMarkers.add(new MarkerData(markers_item, mPt));
        }

        spiderfy(closeMarkers, displayedFarMarkers);
        spiderfiedClusters.add(cluster);
        zoomLevelOnLastSpiderfy = gm.getCameraPosition().zoom;
    }

    private void recursiveAddMarkersToSpiderfy(Marker markers_item) {
        List<Marker> nestedMarkers = markers_item.getMarkers();
        for (Marker nestedMarker : nestedMarkers) {
            if (!nestedMarker.isCluster())
                tryAddMarker(markersInCluster, nestedMarker);
            else
                recursiveAddMarkersToSpiderfy(markers_item);
        }
    }

    private void spiderfy(List<MarkerData> clusteredMarkersData, List<MarkerData> nearbyMarkers) {

        List<MarkerData> listToUse = new ArrayList<MarkerData>();
        listToUse.addAll(clusteredMarkersData);
        listToUse.addAll(nearbyMarkers); //could be terrible... :P
        spiderfying = true;
        int numFeet = listToUse.size();
        List<Point> nearbyMarkerPts = new ArrayList<Point>(numFeet);
        for (MarkerData markerData : listToUse) {
            nearbyMarkerPts.add(markerData.markerPt);
        }
        Point bodyPt = ptAverage(nearbyMarkerPts);
        List<Point> footPts;
        if (numFeet >= circleSpiralSwitchover) {
            footPts = generatePtsSpiral(numFeet, bodyPt);
            Collections.reverse(footPts);
        } else
            footPts = generatePtsCircle(numFeet, bodyPt);

        for (int ind = 0; ind < numFeet; ind++) {
            Point footPt = footPts.get(ind);
            LatLng footLl = ptToLl(footPt);
            MarkerData nearestMarkerData = listToUse.get(ind);
            Marker clusterNearestMarker = nearestMarkerData.marker;
            Polyline leg = gm.addPolyline(new PolylineOptions()
                    .add(clusterNearestMarker.getPosition(), footLl)
                    .color(usual.getType_normal())
                    .width(legWeight)
                    .zIndex(usualLegZIndex));
            omsData.put(clusterNearestMarker, new _omsData()
                    .leg(leg)
                    .usualPosition(clusterNearestMarker.getPosition()));
            clusterNearestMarker.setClusterGroup(ClusterGroup.NOT_CLUSTERED);
            clusterNearestMarker.animatePosition(footLl);

            spiderfiedUnclusteredMarkers.add(clusterNearestMarker);

        }
        isAnythingSpiderfied = true;
        spiderfying = false;
    }

    private Marker unspiderfy(Marker markerToUnspiderfy) { //241
        // this function has to return everything to its original state
        if (markerToUnspiderfy != null) { //Todo: make sure that this "if" is needed at all
            unspiderfying = true;
            if (markerToUnspiderfy.isCluster()) {
                List<Marker> unspiderfiedMarkers = new ArrayList<Marker>(), nonNearbyMarkers = new ArrayList<Marker>();
                for (Marker marker : markersInCluster) {
                    if (omsData.containsKey(marker)) {// ignoring the possibility that (params.markerNotToMove != null)
                        marker.setPosition(omsData.get(marker).leg(null).getUsualPosition());
                        marker.setClusterGroup(ClusterGroup.DEFAULT);
                        //skipped lines 250-254 from original code
                        unspiderfiedMarkers.add(marker);
                    } else
                        nonNearbyMarkers.add(marker);
                }
            } else {
                markerToUnspiderfy.setPosition(omsData.get(markerToUnspiderfy).leg(null).getUsualPosition());
                markerToUnspiderfy.setClusterGroup(ClusterGroup.DEFAULT);
            }
            unspiderfying = false;
        }
        return markerToUnspiderfy; // return self, for chaining
    }

    private int ptDistanceSq(Point pt1, Point pt2) { /** Corresponds to line 264 of original code*/
        int dx = pt1.x - pt2.x;
        int dy = pt1.y - pt2.y;
        return (dx * dx + dy * dy);
    }

    private Point ptAverage(List<Point> pts) { /** Corresponds to line 269 of original code*/
        int sumX = 0, sumY = 0, numPts = pts.size();
        for (Point pt : pts) {
            sumX += pt.x;
            sumY += pt.y;
        }
        return new Point(sumX / numPts, sumY / numPts);
    }


    private Point llToPt(LatLng ll) { /** Corresponds to line 276 of original code*/
        proj = gm.getProjection();
        return proj.toScreenLocation(ll);   // the android maps api equivalent
    }

    private LatLng ptToLl(Point pt) { /** Corresponds to line 277 of original code*/
        proj = gm.getProjection();
        return proj.fromScreenLocation(pt); // the android maps api equivalent
    }

    private void waitForMapIdle() throws InterruptedException {
        while (proj == null) {  // check for "idle" event on map (i.e. no animation is playing)
            Thread.sleep(50);// "Must wait for 'idle' event on map before calling whatever's next"
        }
    }

    private void setSpiderfyalbe(Marker marker, boolean mode) {
        spiderfyable.put(marker, mode);
    }

    private boolean isSpiderfyalbe(Marker marker) {
        return spiderfyable.containsKey(marker) ? spiderfyable.get(marker) : false;
    }

    public boolean isAnythingSpiderfied() {
        return spiderfiedClusters != null;
    }

    private boolean assignVarArgs(Object[] varArgs) {
        int varLen = varArgs.length;
        if (varLen % 2 != 0) {
            throw new IllegalArgumentException("Number of args is uneven.");
        }
        for (int ind = 0; ind < varLen; ind = +2) {
            String key = (String) varArgs[ind];
            if (key.equals(ARG_KEEP_SPIDERFIED)) {
            } else if (key.equals(ARG_MARK_WONT_HIDE)) {
            } else if (key.equals(ARG_MARK_WONT_MOVE)) {
            } else if (key.equals(ARG_NEARBY_DISTANCE)) {
            } else if (key.equals(ARG_CS_SWITCHOVER)) {
            } else if (key.equals(ARG_LEG_WEIGHT)) {
            } else throw new IllegalArgumentException("Invalid argument name.");
        }
        return true;
    }

    private void unspiderfyAll() {
        for (Marker lastSpiderfiedCluster : spiderfiedClusters) {
            unspiderfy(lastSpiderfiedCluster);
        }
        for (Marker marker : spiderfiedUnclusteredMarkers) {
            unspiderfy(marker);
        }
        initMarkerArrays();
        isAnythingSpiderfied = false;
    }

    boolean tryAddMarker(Collection<Marker> collection, Marker obj) {
        if (collection.contains(obj))
            return false;
        else {
            collection.add(obj);
            return true;
        }

    }
}
