/*
 *  Weblounge: Web Content Management System
 *  Copyright (c) 2009 The Weblounge Team
 *  http://weblounge.o2it.ch
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software Foundation
 *  Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package ch.o2it.weblounge.common;

/**
 * Some useful time constants.
 */
public interface Times {

  /** the number of milliseconds per second */
  long MS_PER_SECOND = 1000L;

  /** the number of milliseconds per minute */
  long MS_PER_MIN = 60L * MS_PER_SECOND;

  /** the number of milliseconds per hour */
  long MS_PER_HOUR = 60L * MS_PER_MIN;

  /** the number of milliseconds per day */
  long MS_PER_DAY = 24L * MS_PER_HOUR;

  /** the number of milliseconds per week */
  long MS_PER_WEEK = 7L * MS_PER_DAY;

  /** the number of milliseconds per month */
  long MS_PER_MONTH = 30L * MS_PER_DAY;

  /** the number of milliseconds per year */
  long MS_PER_YEAR = 365L * MS_PER_DAY;
  
  /** the maximum number of miliseconds */
  long MAX_DATE = 9223372036854775000L;

}