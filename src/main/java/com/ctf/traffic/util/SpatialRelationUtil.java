package com.ctf.traffic.util;

import java.util.*;

import com.ctf.traffic.po.*;

import lombok.*;
import lombok.extern.slf4j.*;

@Slf4j
public class SpatialRelationUtil{
    @AllArgsConstructor
    @Data
    public static class Point{
        double x;
        double y;

        public Point(String longitude, String latitude) {
            this.x = Double.valueOf(longitude);
            this.y = Double.valueOf(latitude);
        }
    }

    public static boolean isContains(List<Point> mPoints, Point point) {
        return isPolygonContainsPoint(mPoints, point) || isPointInPolygonBoundary(mPoints, point);
    }

    /**
     * 返回一个点是否在一个多边形区域内
     *
     * @param mPoints 多边形坐标点列表
     * @param point   待判断点
     * @return true 多边形包含这个点,false 多边形未包含这个点。
     */
    public static boolean isPolygonContainsPoint(List<Point> mPoints, Point point) {
        int nCross = 0;
        for (int i = 0; i < mPoints.size(); i++) {
            Point p1 = mPoints.get(i);
            Point p2 = mPoints.get((i + 1) % mPoints.size());
            // 取多边形任意一个边,做点point的水平延长线,求解与当前边的交点个数
            // p1p2是水平线段,要么没有交点,要么有无限个交点
            if (p1.y == p2.y)
                continue;
            // point 在p1p2 底部 --> 无交点
            if (point.y < Math.min(p1.y, p2.y))
                continue;
            // point 在p1p2 顶部 --> 无交点
            if (point.y >= Math.max(p1.y, p2.y))
                continue;
            // 求解 point点水平线与当前p1p2边的交点的 X 坐标
            double x = (point.y - p1.y) * (p2.x - p1.x) / (p2.y - p1.y) + p1.x;
            if (x > point.x) // 当x=point.x时,说明point在p1p2线段上
                nCross++; // 只统计单边交点
        }
        // 单边交点为偶数，点在多边形之外 ---
        return (nCross % 2 == 1);
    }

    /**
     * 返回一个点是否在一个多边形边界上
     *
     * @param mPoints 多边形坐标点列表
     * @param point   待判断点
     * @return true 点在多边形边上,false 点不在多边形边上。
     */
    public static boolean isPointInPolygonBoundary(List<Point> mPoints, Point point) {
        for (int i = 0; i < mPoints.size(); i++) {
            Point p1 = mPoints.get(i);
            Point p2 = mPoints.get((i + 1) % mPoints.size());
            // 取多边形任意一个边,做点point的水平延长线,求解与当前边的交点个数

            // point 在p1p2 底部 --> 无交点
            if (point.y < Math.min(p1.y, p2.y))
                continue;
            // point 在p1p2 顶部 --> 无交点
            if (point.y > Math.max(p1.y, p2.y))
                continue;

            // p1p2是水平线段,要么没有交点,要么有无限个交点
            if (p1.y == p2.y) {
                double minX = Math.min(p1.x, p2.x);
                double maxX = Math.max(p1.x, p2.x);
                // point在水平线段p1p2上,直接return true
                if ((point.y == p1.y) && (point.x >= minX && point.x <= maxX)) {
                    return true;
                }
            } else { // 求解交点
                double x = (point.y - p1.y) * (p2.x - p1.x) / (p2.y - p1.y) + p1.x;
                if (x == point.x) // 当x=point.x时,说明point在p1p2线段上
                    return true;
            }
        }
        return false;
    }

    public static List<Point> toList(String str) {
        List<Point> points = new ArrayList<>();
        final String[] split = str.split(",");
        for (int i = 0; i < split.length - 1; i++) {
            points.add(new Point(Double.valueOf(split[i]), Double.valueOf(split[++i])));
        }
        return points;
    }

    public static Long getSubstation(List<Substation> substations, Point point) {
        for (int i = 0; i < substations.size(); i++) {
            Substation substation = substations.get(i);
            String roundArea = substation.getRoundArea();
            String[] split = roundArea.split("-");
            for (int j = 0; j < split.length; j++) {
                if (SpatialRelationUtil.isContains(SpatialRelationUtil.toList(split[j]), point)) {
                    return substation.getId();
                }
            }
        }
        return null;
    }

}