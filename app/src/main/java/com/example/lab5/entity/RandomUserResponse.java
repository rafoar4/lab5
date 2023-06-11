package com.example.lab5.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RandomUserResponse {
    @SerializedName("results")
    private List<RandomUser> results;

    public List<RandomUser> getResults() {
        return results;
    }

    public class RandomUser {
        private String gender;
        private Name name;
        private Location location;
        private String email;
        private String phone;
        private String cell;
        private Picture picture;
        private Dob dob;
        private String nat;

        public String getNat() {
            return nat;
        }

        public void setNat(String nat) {
            this.nat = nat;
        }

        public Dob getDob() {
            return dob;
        }

        public void setDob(Dob dob) {
            this.dob = dob;
        }

        public String getGender() {
            return gender;
        }

        public Name getName() {
            return name;
        }

        public Location getLocation() {
            return location;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getCell() {
            return cell;
        }

        public Picture getPicture() {
            return picture;
        }

        public class Name {
            private String title;
            private String first;
            private String last;

            public String getTitle() {
                return title;
            }

            public String getFirst() {
                return first;
            }

            public String getLast() {
                return last;
            }
        }

        public class Location {
            private Street street;
            private String city;
            private String state;
            private String country;
            private String postcode;

            public Street getStreet() {
                return street;
            }

            public String getCity() {
                return city;
            }

            public String getState() {
                return state;
            }

            public String getCountry() {
                return country;
            }

            public String getPostcode() {
                return postcode;
            }

            public class Street {
                private int number;
                private String name;

                public int getNumber() {
                    return number;
                }

                public String getName() {
                    return name;
                }
            }
        }

        public class Picture {
            private String large;
            private String medium;
            private String thumbnail;

            public String getLarge() {
                return large;
            }

            public String getMedium() {
                return medium;
            }

            public String getThumbnail() {
                return thumbnail;
            }
        }

        public class Dob{
            private int age;

            public int getAge() {
                return age;
            }

            public void setAge(int age) {
                this.age = age;
            }
        }
    }
}

