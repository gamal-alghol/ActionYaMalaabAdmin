package com.malaab.ya.action.actionyamalaab.admin.utils;

import android.location.Location;

import com.malaab.ya.action.actionyamalaab.admin.annotations.SortOption;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class ListUtils {

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }


    public static List<User> sortUsersList(List<User> list, @SortOption int option) {
        List<User> users = new ArrayList<>(list);

        if (option == SortOption.RECENT) {
            Collections.sort(users, new Comparator<User>() {
                @Override
                public int compare(User a, User b) {
                    return Float.compare(a.createdDate, b.createdDate);
                }
            });

            Collections.reverse(users);

        } else if (option == SortOption.DEACTIVATED) {
            users.clear();
            for (User user : list) {
                if (!user.isActive) {
                    users.add(user);
                }
            }

        } else if (option == SortOption.NO_OF_BOOKINGS) {
            Collections.sort(users, new Comparator<User>() {
                @Override
                public int compare(User a, User b) {
                    return Float.compare(a.noOfBookings, b.noOfBookings);
                }
            });

            Collections.reverse(users);
        }

        return users;
    }


//    public static List<Booking> sortBookingsListDesc(List<Booking> list) {
//        List<Booking> bookings = new ArrayList<>(list);
//
//        Collections.sort(bookings, new Comparator<Booking>() {
//            @Override
//            public int compare(Booking a, Booking b) {
//                return Float.compare(b.timeStart, a.timeStart);
//            }
//        });
//
//        return bookings;
//    }

    public static List<Booking> sortBookingsListAsc(List<Booking> bookings) {
        Collections.sort(bookings, new Comparator<Booking>() {
            @Override
            public int compare(Booking a, Booking b) {
                Date dateStart = new Date();
                dateStart.setTime(a.timeStart);

                Date dateEnd = new Date();
                dateEnd.setTime(b.timeStart);

                return dateStart.compareTo(dateEnd);
            }
        });

        return bookings;
    }

    public static List<Booking> sortBookingsListDesc(List<Booking> bookings) {
        Collections.sort(bookings, new Comparator<Booking>() {
            @Override
            public int compare(Booking a, Booking b) {
                Date dateStart = new Date();
                dateStart.setTime(a.timeStart);

                Date dateEnd = new Date();
                dateEnd.setTime(b.timeStart);

                return dateEnd.compareTo(dateStart);
            }
        });

        return bookings;
    }


    public static List<Playground> sortPlaygroundsList(ArrayList<Playground> list, final Location userCurrentLocation) {
        List<Playground> playgrounds = new ArrayList<>(list);

        Collections.sort(playgrounds, new Comparator<Playground>() {
            @Override
            public int compare(Playground a, Playground b) {
                Location locationA = new Location("point A");
                locationA.setLatitude(a.latitude);
                locationA.setLongitude(a.longitude);

                Location locationB = new Location("point B");
                locationB.setLatitude(b.latitude);
                locationB.setLongitude(b.longitude);

                float distanceOne = userCurrentLocation.distanceTo(locationA);
                float distanceTwo = userCurrentLocation.distanceTo(locationB);

                return Float.compare(distanceOne, distanceTwo);
            }
        });

        return playgrounds;

//        // Ascending Order
//        Collections.sort(calls, new Comparator<Playground>() {
//            @Override
//            public int compare(Playground o1, Playground o2) {
//                return (int)(o1.ms-o2.ms);
//            }
//        });

//        // Descending Order
//        Collections.sort(calls, new Comparator<Playground>() {
//
//            @Override
//            public int compare(Playground o1, Playground o2) {
//                return (int)(o2.ms-o1.ms);
//            }
//        });
    }


//    public static List<NewArrivalsProducts.Item> cloneNewArrivalsProductsList(List<NewArrivalsProducts.Item> items) {
//        List<NewArrivalsProducts.Item> clonedList = new ArrayList<>(items.size());
//        for (NewArrivalsProducts.Item item : items) {
//            clonedList.add(new NewArrivalsProducts.Item(item));
//        }
//        return clonedList;
//    }
}
