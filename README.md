# Calendar Service

A simple Calendar API for managing events.

## Overview

The Calendar Service should allow users to manage their events, including creating, updating, deleting, and viewing events. 
Events should support time zones and basic recurrence.

---

## Requirements

- Users can create, read, update, and delete events.
- Events must include:
    - Title
    - Description
    - Start and end times
    - Optional recurrence information
- Events should be stored in UTC.
- Users may have a preferred time zone for viewing events.
- API should allow filtering events by user and by date/time.
- Each user should only access their own events.
- Optional: support for recurring events (daily, weekly, monthly).  
